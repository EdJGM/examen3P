package ec.edu.espe.ms_inventario.service;


import ec.edu.espe.ms_inventario.dto.EventoCosechaDTO;
import ec.edu.espe.ms_inventario.dto.InsumoDTO;
import ec.edu.espe.ms_inventario.entity.Insumo;
import ec.edu.espe.ms_inventario.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private NotificacionProducerService notificacionProducer;

    // Fórmulas de cálculo de insumos por producto
    private static final Map<String, List<RequerimientoInsumo>> FORMULAS_PRODUCTOS = Map.of(
            "Arroz Oro", List.of(
                    new RequerimientoInsumo("Semilla Arroz L-23", 5.0), // 5kg semilla por tonelada
                    new RequerimientoInsumo("Fertilizante N-PK", 2.0)   // 2kg fertilizante por tonelada
            ),
            "Café Premium", List.of(
                    new RequerimientoInsumo("Fertilizante N-PK", 3.0),
                    new RequerimientoInsumo("Pesticida Orgánico", 0.5)
            )
    );

    public List<Insumo> listarTodos() {
        return insumoRepository.findAll();
    }

    public List<Insumo> listarDisponibles() {
        return insumoRepository.findInsumosDisponibles();
    }

    public List<Insumo> listarConStockBajo() {
        return insumoRepository.findInsumosConStockBajo();
    }

    public Insumo buscarPorId(UUID id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo con id " + id + " no encontrado"));
    }

    public Insumo buscarPorNombre(String nombre) {
        return insumoRepository.findByNombreInsumo(nombre)
                .orElseThrow(() -> new RuntimeException("Insumo " + nombre + " no encontrado"));
    }

    public Insumo crear(InsumoDTO dto) {
        // Verificar que no existe otro insumo con el mismo nombre
        if (insumoRepository.findByNombreInsumo(dto.getNombreInsumo()).isPresent()) {
            throw new RuntimeException("Ya existe un insumo con el nombre: " + dto.getNombreInsumo());
        }

        Insumo insumo = new Insumo();
        mapearDtoAEntidad(dto, insumo);

        Insumo saved = insumoRepository.save(insumo);

        // Notificar creación
        notificacionProducer.enviarNotificacion(
                "Nuevo insumo registrado: " + saved.getNombreInsumo(),
                "nuevo_insumo"
        );

        return saved;
    }

    public Insumo actualizar(UUID id, InsumoDTO dto) {
        Insumo insumo = buscarPorId(id);
        mapearDtoAEntidad(dto, insumo);
        return insumoRepository.save(insumo);
    }

    public Insumo actualizarStock(UUID id, Integer nuevoStock) {
        Insumo insumo = buscarPorId(id);
        insumo.setStock(nuevoStock);
        return insumoRepository.save(insumo);
    }

    public void eliminar(UUID id) {
        Insumo insumo = buscarPorId(id);
        insumoRepository.delete(insumo);
    }

    /**
     * Procesa una nueva cosecha y ajusta el inventario de insumos
     */
    public void procesarCosecha(EventoCosechaDTO evento) {
        try {
            String producto = evento.getProducto();
            BigDecimal toneladas = evento.getToneladas();

            System.out.println("🌾 Procesando cosecha: " + producto + " - " + toneladas + "t");

            // Obtener fórmula de insumos para el producto
            List<RequerimientoInsumo> requerimientos = FORMULAS_PRODUCTOS.getOrDefault(
                    producto, FORMULAS_PRODUCTOS.get("Arroz Oro") // Default
            );

            // Procesar cada insumo requerido
            for (RequerimientoInsumo req : requerimientos) {
                int cantidadRequerida = (int) Math.ceil(toneladas.doubleValue() * req.getRatioPorTonelada());
                procesarInsumo(req.getNombreInsumo(), cantidadRequerida);
            }

            // Verificar stock bajo después del ajuste
            verificarStockBajo();

            System.out.println("✅ Inventario ajustado correctamente para cosecha: " + evento.getCosechaId());

        } catch (Exception e) {
            System.err.println("❌ Error procesando cosecha: " + e.getMessage());

            // Notificar error
            notificacionProducer.enviarNotificacion(
                    "Error ajustando inventario para cosecha " + evento.getCosechaId() + ": " + e.getMessage(),
                    "error_inventario"
            );

            throw e;
        }
    }

    private void procesarInsumo(String nombreInsumo, int cantidadRequerida) {
        try {
            Insumo insumo = buscarPorNombre(nombreInsumo);

            if (!insumo.tieneStockSuficiente(cantidadRequerida)) {
                String mensaje = String.format(
                        "Stock insuficiente: %s. Disponible: %d, Requerido: %d",
                        nombreInsumo, insumo.getStock(), cantidadRequerida
                );

                // Notificar stock insuficiente
                notificacionProducer.enviarNotificacion(mensaje, "stock_insuficiente");
                throw new RuntimeException(mensaje);
            }

            // Descontar stock
            insumo.descontarStock(cantidadRequerida);
            insumoRepository.save(insumo);

            System.out.println(String.format(
                    "📦 Stock actualizado: %s -%d%s = %d%s restante",
                    nombreInsumo, cantidadRequerida, insumo.getUnidadMedida(),
                    insumo.getStock(), insumo.getUnidadMedida()
            ));

        } catch (RuntimeException e) {
            throw e; // Re-throw para manejar en nivel superior
        }
    }

    private void verificarStockBajo() {
        List<Insumo> insumosStockBajo = listarConStockBajo();

        for (Insumo insumo : insumosStockBajo) {
            notificacionProducer.enviarNotificacion(
                    String.format("⚠️ Stock bajo: %s (%d%s restante, mínimo: %d)",
                            insumo.getNombreInsumo(), insumo.getStock(),
                            insumo.getUnidadMedida(), insumo.getStockMinimo()),
                    "stock_bajo"
            );
        }
    }

    private void mapearDtoAEntidad(InsumoDTO dto, Insumo insumo) {
        insumo.setNombreInsumo(dto.getNombreInsumo());
        insumo.setStock(dto.getStock() != null ? dto.getStock() : 0);
        insumo.setUnidadMedida(dto.getUnidadMedida() != null ? dto.getUnidadMedida() : "kg");
        insumo.setCategoria(dto.getCategoria());
        insumo.setPrecioUnitario(dto.getPrecioUnitario() != null ? dto.getPrecioUnitario() : 0.0);
        insumo.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 10);
    }

    // Clase auxiliar para requerimientos
    private static class RequerimientoInsumo {
        private final String nombreInsumo;
        private final double ratioPorTonelada;

        public RequerimientoInsumo(String nombreInsumo, double ratioPorTonelada) {
            this.nombreInsumo = nombreInsumo;
            this.ratioPorTonelada = ratioPorTonelada;
        }

        public String getNombreInsumo() { return nombreInsumo; }
        public double getRatioPorTonelada() { return ratioPorTonelada; }
    }
}
