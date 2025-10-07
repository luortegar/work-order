import { Document, Page, pdfjs } from 'react-pdf';
import { useState, useRef, useEffect } from 'react';
import { Box } from '@mui/material';
import 'react-pdf/dist/Page/AnnotationLayer.css';
import 'react-pdf/dist/Page/TextLayer.css';

pdfjs.GlobalWorkerOptions.workerSrc = `https://unpkg.com/pdfjs-dist@${pdfjs.version}/build/pdf.worker.min.mjs`;

type PDFViewerProps = {
  pdfUrl: string;
};

export default function PDFViewer({ pdfUrl }: PDFViewerProps) {
  const [numPages, setNumPages] = useState<number | null>(null);
  const [width, setWidth] = useState<number>(0);
  const containerRef = useRef<HTMLDivElement>(null);

  // Ajuste dinámico del ancho del contenedor
  useEffect(() => {
    const handleResize = () => {
      if (containerRef.current) {
        setWidth(containerRef.current.offsetWidth);
      }
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <Box
      ref={containerRef}
      sx={{
        height: 'calc(90vh - 100px)',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundColor: '#f0f0f0', // fondo gris claro
        p: 3,
      }}
    >
      <Document
        file={pdfUrl}
        onLoadSuccess={({ numPages }) => setNumPages(numPages)}
        onLoadError={(err) => console.error('Error al cargar PDF:', err)}
        loading="Cargando PDF..."
      >
        {numPages &&
          Array.from({ length: numPages }, (_, i) => (
            <Box
              key={`page_${i + 1}`}
              sx={{
                mb: 3, // separación entre páginas
                boxShadow: '0 2px 8px rgba(0,0,0,0.15)', // sombra leve
                borderRadius: 1,
                backgroundColor: 'white',
              }}
            >
              <Page
                pageNumber={i + 1}
                width={width > 0 ? Math.min(width - 48, 900) : undefined}
                renderAnnotationLayer={false}
                renderTextLayer={false}
              />
            </Box>
          ))}
      </Document>
    </Box>
  );
}
