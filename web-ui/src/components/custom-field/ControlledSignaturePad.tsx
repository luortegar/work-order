import React, { useCallback, useEffect, useRef, useState } from "react";
import { useController, Control } from "react-hook-form";
import {
  Box,
  Button,
  Card,
  Stack,
  Typography,
  useTheme,
} from "@mui/material";

interface ControlledSignaturePadProps {
  name: string;
  control: Control<any>;
  height?: number;
  lineWidth?: number;
  strokeStyle?: string;
  backgroundColor?: string;
  disabled?: boolean;
  label?: string;
}

const ControlledSignaturePad: React.FC<ControlledSignaturePadProps> = ({
  name,
  control,
  height = 200,
  lineWidth = 2,
  strokeStyle = "#111827",
  backgroundColor = "#ffffff",
  disabled = false,
  label,
}) => {
  const {
    field: { value, onChange },
  } = useController({ name, control });

  const theme = useTheme();
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const ctxRef = useRef<CanvasRenderingContext2D | null>(null);
  const drawingRef = useRef(false);
  const lastPointRef = useRef<{ x: number; y: number } | null>(null);
  const [signed, setSigned] = useState(false);

  const dpr =
    typeof window !== "undefined" ? Math.max(1, window.devicePixelRatio || 1) : 1;

  /** Inicializa el canvas para que use el ancho real del contenedor */
  const initCanvas = useCallback(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const realWidth = canvas.offsetWidth; // ancho actual del contenedor
    canvas.width = Math.floor(realWidth * dpr);
    canvas.height = Math.floor(height * dpr);
    canvas.style.width = "100%";
    canvas.style.height = `${height}px`;

    const ctx = canvas.getContext("2d");
    if (!ctx) return;
    ctxRef.current = ctx;

    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.scale(dpr, dpr);
    ctx.fillStyle = backgroundColor;
    ctx.fillRect(0, 0, realWidth, height);
    ctx.lineCap = "round";
    ctx.lineJoin = "round";
    ctx.lineWidth = lineWidth;
    ctx.strokeStyle = strokeStyle;
  }, [height, dpr, backgroundColor, lineWidth, strokeStyle]);

  useEffect(() => {
    initCanvas();
    // Redibuja si cambia el tamaño de la ventana
    const handleResize = () => initCanvas();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [initCanvas]);

  // Carga la imagen si ya hay firma guardada
  useEffect(() => {
    if (!value) return;
    const canvas = canvasRef.current;
    const ctx = ctxRef.current;
    if (!canvas || !ctx) return;

    const image = new Image();
    image.src = value;
    image.onload = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      initCanvas();
      ctx.drawImage(image, 0, 0, canvas.width / dpr, canvas.height / dpr);
      setSigned(true);
    };
  }, [value, initCanvas, dpr]);

  const getPos = (e: MouseEvent | TouchEvent) => {
    const canvas = canvasRef.current!;
    const rect = canvas.getBoundingClientRect();
    if (e instanceof TouchEvent) {
      const t = e.touches[0] || e.changedTouches[0];
      return { x: t.clientX - rect.left, y: t.clientY - rect.top };
    }
    const m = e as MouseEvent;
    return { x: m.clientX - rect.left, y: m.clientY - rect.top };
  };

  const getImageBase64 = useCallback((): string | null => {
    const canvas = canvasRef.current;
    return canvas?.toDataURL("image/png") ?? null;
  }, []);

  const startDrawing = (e: MouseEvent | TouchEvent) => {
    if (disabled) return;
    drawingRef.current = true;
    lastPointRef.current = getPos(e);
  };

  const draw = (e: MouseEvent | TouchEvent) => {
    if (!drawingRef.current || !ctxRef.current || disabled) return;
    const { x, y } = getPos(e);
    const last = lastPointRef.current ?? { x, y };
    const ctx = ctxRef.current;
    ctx.beginPath();
    ctx.moveTo(last.x, last.y);
    ctx.lineTo(x, y);
    ctx.stroke();
    lastPointRef.current = { x, y };
    if (!signed) setSigned(true);
    e.preventDefault?.();
  };

  const endDrawing = () => {
    if (!drawingRef.current) return;
    drawingRef.current = false;
    lastPointRef.current = null;
    onChange(getImageBase64());
  };

  const clear = useCallback(() => {
    const ctx = ctxRef.current;
    if (!ctx) return;
    ctx.save();
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.restore();
    initCanvas();
    setSigned(false);
    onChange(null);
  }, [initCanvas, onChange]);

  // Eventos de dibujo
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const md = (e: MouseEvent) => startDrawing(e);
    const mm = (e: MouseEvent) => draw(e);
    const mu = () => endDrawing();
    const ml = () => endDrawing();

    const ts = (e: TouchEvent) => { e.preventDefault(); startDrawing(e); };
    const tm = (e: TouchEvent) => draw(e);
    const te = () => endDrawing();

    canvas.addEventListener("mousedown", md);
    canvas.addEventListener("mousemove", mm);
    window.addEventListener("mouseup", mu);
    canvas.addEventListener("mouseleave", ml);

    canvas.addEventListener("touchstart", ts, { passive: false });
    canvas.addEventListener("touchmove", tm, { passive: false });
    window.addEventListener("touchend", te);

    return () => {
      canvas.removeEventListener("mousedown", md);
      canvas.removeEventListener("mousemove", mm);
      window.removeEventListener("mouseup", mu);
      canvas.removeEventListener("mouseleave", ml);
      canvas.removeEventListener("touchstart", ts);
      canvas.removeEventListener("touchmove", tm);
      window.removeEventListener("touchend", te);
    };
  }, [draw]);

  return (
    <Card
      elevation={3}
      sx={{
        p: 2,
        borderRadius: "8px",
        width: "100%", // ✅ ancho total
        bgcolor: disabled ? theme.palette.grey[100] : "background.paper",
      }}
    >
      {label && (
        <Typography variant="body2" color="text.secondary">
          {label}
        </Typography>
      )}

      <Stack spacing={2}>
        <Box
          sx={{
            border: `2px dashed ${theme.palette.secondary.main}`,
            borderRadius: 1,
            overflow: "hidden",
            width: "100%", // ✅ ancho total
            height,        // altura fija o la que recibas por prop
            position: "relative",
            bgcolor: disabled ? theme.palette.grey[100] : "#fafafa",
          }}
        >
          <canvas
            ref={canvasRef}
            style={{
              width: "100%",      // ✅ ancho total
              height: `${height}px`,
              backgroundColor,
              opacity: disabled ? 0.6 : 1,
              cursor: disabled ? "not-allowed" : "crosshair",
              display: "block",
            }}
          />
        </Box>

        <Stack direction="row" justifyContent="space-between" alignItems="center">
          <Typography
            variant="body2"
            color={signed ? "success.main" : "text.secondary"}
          >
            {signed ? "Signature captured" : "Still no signature"}
          </Typography>
          <Button
            size="small"
            variant="outlined"
            color="secondary"
            onClick={clear}
            disabled={disabled}
          >
            Limpiar firma
          </Button>
        </Stack>
      </Stack>
    </Card>
  );
};

export default ControlledSignaturePad;
