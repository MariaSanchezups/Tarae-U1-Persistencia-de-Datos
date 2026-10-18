package com.mycompany.ormestudiantes;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProductoForm extends JFrame {

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtCategoria = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JTextField txtSucursal = new JTextField();
    private final JTextField txtBuscar = new JTextField(15);

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new String[]{"ID", "Código", "Nombre",
                        "Categoría", "Precio", "Stock", "Sucursal"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };

    private final JTable tabla = new JTable(modelo);

    public ProductoForm() {
        setTitle("TUTI - Gestión de Productos");
        setSize(1050, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
        cargarProductos();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                HibernateUtil.cerrar();
                dispose();
            }
        });
    }

    private void crearInterfaz() {
        JPanel formulario = new JPanel(new GridLayout(6, 2, 8, 8));
        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del producto"));

        formulario.add(new JLabel("Código:"));
        formulario.add(txtCodigo);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Categoría:"));
        formulario.add(txtCategoria);

        formulario.add(new JLabel("Precio ($):"));
        formulario.add(txtPrecio);

        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        formulario.add(new JLabel("Sucursal:"));
        formulario.add(txtSucursal);

        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnCrear.addActionListener(e -> crearProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrar = new JButton("Mostrar todos");

        btnBuscar.addActionListener(e -> buscarProductos());
        btnMostrar.addActionListener(e -> cargarProductos());

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.add(new JLabel("Buscar por nombre:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);
        busqueda.add(btnMostrar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() >= 0) {

                int fila = tabla.getSelectedRow();

                txtCodigo.setText(modelo.getValueAt(fila, 1).toString());
                txtNombre.setText(modelo.getValueAt(fila, 2).toString());
                txtCategoria.setText(modelo.getValueAt(fila, 3).toString());
                txtPrecio.setText(modelo.getValueAt(fila, 4).toString());
                txtStock.setText(modelo.getValueAt(fila, 5).toString());
                txtSucursal.setText(modelo.getValueAt(fila, 6).toString());
            }
        });

        JPanel superior = new JPanel(new BorderLayout(8, 8));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(busqueda, BorderLayout.SOUTH);
    }

    private Producto leerFormulario() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String sucursal = txtSucursal.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()
                || categoria.isEmpty() || sucursal.isEmpty()) {
            throw new IllegalArgumentException(
                    "Completa el código, nombre, categoría y sucursal.");
        }

        double precio = Double.parseDouble(
                txtPrecio.getText().trim().replace(',', '.'));
        int stock = Integer.parseInt(txtStock.getText().trim());

        if (!Double.isFinite(precio) || precio < 0 || stock < 0) {
            throw new IllegalArgumentException(
                    "El precio y el stock no pueden ser negativos.");
        }

        return new Producto(
                codigo, nombre, categoria, precio, stock, sucursal);
    }

    // CREATE: registrar un producto
    private void crearProducto() {
        try {
            Producto producto = leerFormulario();

            try (Session sesion = HibernateUtil
                    .getSessionFactory().openSession()) {

                Transaction tx = sesion.beginTransaction();

                try {
                    sesion.persist(producto);
                    tx.commit();
                } catch (Exception ex) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw ex;
                }
            }

            cargarProductos();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Producto registrado correctamente.");

        } catch (NumberFormatException ex) {
            mensajeError("Ingresa un precio y un stock válidos.");
        } catch (Exception ex) {
            mensajeError(ex.getMessage());
        }
    }

    // READ: consultar todos los productos
    private void cargarProductos() {
        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Producto> productos = sesion.createQuery(
                    "FROM Producto ORDER BY id", Producto.class)
                    .getResultList();

            mostrarProductos(productos);

        } catch (Exception ex) {
            mensajeError(ex.getMessage());
        }
    }

    // READ: buscar productos por nombre
    private void buscarProductos() {
        try (Session sesion = HibernateUtil
                .getSessionFactory().openSession()) {

            List<Producto> productos = sesion.createQuery(
                    "FROM Producto WHERE LOWER(nombre) "
                    + "LIKE :nombre ORDER BY id", Producto.class)
                    .setParameter("nombre",
                            "%" + txtBuscar.getText()
                                    .trim().toLowerCase() + "%")
                    .getResultList();

            mostrarProductos(productos);

        } catch (Exception ex) {
            mensajeError(ex.getMessage());
        }
    }

    // UPDATE: actualizar un producto seleccionado
    private void actualizarProducto() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            mensajeError("Selecciona un producto de la tabla.");
            return;
        }

        try {
            Long id = Long.valueOf(
                    modelo.getValueAt(fila, 0).toString());

            Producto datos = leerFormulario();

            try (Session sesion = HibernateUtil
                    .getSessionFactory().openSession()) {

                Transaction tx = sesion.beginTransaction();

                try {
                    Producto producto = sesion.find(Producto.class, id);

                    if (producto == null) {
                        throw new IllegalArgumentException(
                                "El producto ya no existe.");
                    }

                    producto.setCodigo(datos.getCodigo());
                    producto.setNombre(datos.getNombre());
                    producto.setCategoria(datos.getCategoria());
                    producto.setPrecio(datos.getPrecio());
                    producto.setStock(datos.getStock());
                    producto.setSucursal(datos.getSucursal());

                    tx.commit();

                } catch (Exception ex) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw ex;
                }
            }

            cargarProductos();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Producto actualizado correctamente.");

        } catch (NumberFormatException ex) {
            mensajeError("Revisa el precio y el stock.");
        } catch (Exception ex) {
            mensajeError(ex.getMessage());
        }
    }

    // DELETE: eliminar un producto seleccionado
    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            mensajeError("Selecciona un producto de la tabla.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar el producto seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.valueOf(
                    modelo.getValueAt(fila, 0).toString());

            try (Session sesion = HibernateUtil
                    .getSessionFactory().openSession()) {

                Transaction tx = sesion.beginTransaction();

                try {
                    Producto producto = sesion.find(Producto.class, id);

                    if (producto != null) {
                        sesion.remove(producto);
                    }

                    tx.commit();

                } catch (Exception ex) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw ex;
                }
            }

            cargarProductos();
            limpiarCampos();

            JOptionPane.showMessageDialog(this,
                    "Producto eliminado correctamente.");

        } catch (Exception ex) {
            mensajeError(ex.getMessage());
        }
    }

    private void mostrarProductos(List<Producto> productos) {
        modelo.setRowCount(0);

        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                p.getPrecio(),
                p.getStock(),
                p.getSucursal()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtSucursal.setText("");
        tabla.clearSelection();
    }
    private void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje == null ? "Ocurrió un error." : mensaje,
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
    }
}

