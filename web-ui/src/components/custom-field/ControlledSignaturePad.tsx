import React, { useCallback, useEffect, useRef, useState } from "react";
import { useController, Control } from "react-hook-form";
import {
  Box,
  Button,
  Card,
  Paper,
  Stack,
  Typography,
  useTheme,
} from "@mui/material";

interface ControlledSignaturePadProps {
  name: string;
  control: Control<any>;
  width?: number;
  height?: number;
  lineWidth?: number;
  strokeStyle?: string;
  backgroundColor?: string;
  disabled?: boolean;
  label?:string
}

const ControlledSignaturePad: React.FC<ControlledSignaturePadProps> = ({
  name,
  control,
  width = 500,
  height = 200,
  lineWidth = 2,
  strokeStyle = "#111827",
  backgroundColor = "#ffffff",
  disabled = false,
  label
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

  const initCanvas = useCallback(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    canvas.width = Math.floor(width * dpr);
    canvas.height = Math.floor(height * dpr);
    canvas.style.width = `${width}px`;
    canvas.style.height = `${height}px`;

    const ctx = canvas.getContext("2d");
    if (!ctx) return;
    ctxRef.current = ctx;

    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.scale(dpr, dpr);

    ctx.fillStyle = backgroundColor;
    ctx.fillRect(0, 0, width, height);

    ctx.lineCap = "round";
    ctx.lineJoin = "round";
    ctx.lineWidth = lineWidth;
    ctx.strokeStyle = strokeStyle;
  }, [width, height, dpr, backgroundColor, lineWidth, strokeStyle]);

  // Inicializa canvas
  useEffect(() => {
    initCanvas();
  }, [initCanvas]);

  // Si value tiene base64, se carga en el canvas (modo edición)
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
      ctx.drawImage(image, 0, 0, width, height);
      setSigned(true);
    };
  }, [value, initCanvas, width, height]);

  const getPos = (e: MouseEvent | TouchEvent) => {
    const canvas = canvasRef.current!;
    const rect = canvas.getBoundingClientRect();
    let x = 0,
      y = 0;
    if (e instanceof TouchEvent) {
      const t = e.touches[0] || e.changedTouches[0];
      x = t.clientX - rect.left;
      y = t.clientY - rect.top;
    } else {
      const m = e as MouseEvent;
      x = m.clientX - rect.left;
      y = m.clientY - rect.top;
    }
    return { x, y };
  };

  const getImageBase64 = useCallback((): string | null => {
    const canvas = canvasRef.current;
    if (!canvas) return null;
    return canvas.toDataURL("image/png");
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

  // Eventos de mouse/touch
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const handleMouseDown = (e: MouseEvent) => startDrawing(e);
    const handleMouseMove = (e: MouseEvent) => draw(e);
    const handleMouseUp = () => endDrawing();
    const handleMouseLeave = () => endDrawing();

    const handleTouchStart = (e: TouchEvent) => {
      e.preventDefault();
      startDrawing(e);
    };
    const handleTouchMove = (e: TouchEvent) => draw(e);
    const handleTouchEnd = () => endDrawing();

    canvas.addEventListener("mousedown", handleMouseDown);
    canvas.addEventListener("mousemove", handleMouseMove);
    window.addEventListener("mouseup", handleMouseUp);
    canvas.addEventListener("mouseleave", handleMouseLeave);

    canvas.addEventListener("touchstart", handleTouchStart, { passive: false });
    canvas.addEventListener("touchmove", handleTouchMove, { passive: false });
    window.addEventListener("touchend", handleTouchEnd);

    return () => {
      canvas.removeEventListener("mousedown", handleMouseDown);
      canvas.removeEventListener("mousemove", handleMouseMove);
      window.removeEventListener("mouseup", handleMouseUp);
      canvas.removeEventListener("mouseleave", handleMouseLeave);

      canvas.removeEventListener("touchstart", handleTouchStart);
      canvas.removeEventListener("touchmove", handleTouchMove);
      window.removeEventListener("touchend", handleTouchEnd);
    };
  }, [draw]);

  return (
    <Card
      elevation={3}
      sx={{
        p: 2,
        borderRadius: "8px",
        width: width + 35,
        bgcolor: disabled ? theme.palette.grey[100] : "background.paper",
      }}
    >
    <Typography
        variant="body2"
        color={"text.secondary"}
    >
        {label}
    </Typography>
      <Stack spacing={2}>
        {/* Área de firma */}
        <Box
          sx={{
            border: `2px dashed ${theme.palette.secondary.main}`,
            borderRadius: 1,
            overflow: "hidden",
            position: "relative",
            width,
            height,
            display: "inline-block",
            bgcolor: disabled ? theme.palette.grey[100] : "#fafafa",
          }}
        >
          <canvas
            ref={canvasRef}
            style={{
              width,
              height,
              backgroundColor,
              opacity: disabled ? 0.6 : 1,
              cursor: disabled ? "not-allowed" : "crosshair",
              display: "block",
            }}
          />
        </Box>

        {/* Estado + Botón limpiar */}
        <Stack direction="row" justifyContent="space-between" alignItems="center">
          <Typography
            variant="body2"
            color={signed ? "success.main" : "text.secondary"}
          >
            {signed ? "Signature captured": "Still no signature"}
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
