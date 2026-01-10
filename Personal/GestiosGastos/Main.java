package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main extends JFrame {

    static class Gasto {
        private String name;
        private int valor;

        public static ArrayList<Gasto> gastos = new ArrayList<>();
        public static ArrayList<Gasto> ingresos = new ArrayList<>();

        public Gasto(String name, int valor) {
            this.name = name;
            this.valor = valor;
        }

        public String getName() {
            return name;
        }

        public int getValor() {
            return valor;
        }

        public void setValor(int valor) {
            this.valor = valor;
        }

        @Override
        public String toString() {
            return name + " - " + valor;
        }
    }

    // Componentes UI
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel labelTotalGastos;
    private JLabel labelTotalIngresos;
    private JLabel labelSaldo;

    public Main() {
        setTitle("Programa de Gastos e Ingresos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");

        JMenu añadir = new JMenu("añadir");
        JMenu Gasto = new JMenu("Gasto");

        JMenuItem addGastoItemMensual = new JMenuItem("Gasto mensual");
        JMenuItem addGastoItemTrimestral = new JMenuItem("Gasto trimestral");
        JMenuItem addGastoItemOther = new JMenuItem("Gasto de otro valor");
        JMenuItem addIngresoItem = new JMenuItem("Ingreso");
        JMenuItem modificarGastoItem = new JMenuItem("Modificar Gasto");
        JMenuItem cargarExcelItem = new JMenuItem("Leer datos de Excel");
        JMenuItem guardarExcelItem = new JMenuItem("Guardar datos a Excel");
        JMenuItem salirItem = new JMenuItem("Salir");

        añadir.add(addIngresoItem);
        Gasto.add(addGastoItemMensual);
        Gasto.add(addGastoItemTrimestral);
        Gasto.add(addGastoItemOther);
        añadir.add(Gasto);

        menu.add(modificarGastoItem);
        menu.addSeparator();
        menu.add(cargarExcelItem);
        menu.add(guardarExcelItem);
        menu.addSeparator();
        menu.add(salirItem);
        menuBar.add(menu);
        menuBar.add(añadir);
        setJMenuBar(menuBar);



        // Tabla para mostrar datos
        tableModel = new DefaultTableModel(new Object[]{"Tipo", "Nombre", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable directamente
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel resumen
        JPanel panelResumen = new JPanel(new GridLayout(1, 3, 10, 10));
        labelTotalGastos = new JLabel("Total Gastos: 0");
        labelTotalIngresos = new JLabel("Total Ingresos: 0");
        labelSaldo = new JLabel("Saldo (Ingresos - Gastos): 0");
        labelTotalGastos.setHorizontalAlignment(SwingConstants.CENTER);
        labelTotalIngresos.setHorizontalAlignment(SwingConstants.CENTER);
        labelSaldo.setHorizontalAlignment(SwingConstants.CENTER);
        panelResumen.add(labelTotalGastos);
        panelResumen.add(labelTotalIngresos);
        panelResumen.add(labelSaldo);

        // Layout principal
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelResumen, BorderLayout.SOUTH);

        // Acciones menu
        addGastoItemMensual.addActionListener(e -> anadirEntrada(true));
        addGastoItemTrimestral.addActionListener(e -> anadirEntrada(true));//si es trimestral, dividir el valor en 3;
        addGastoItemOther.addActionListener(e -> anadirEntrada(true));//pedir valor de cada cuantos meses;
        addIngresoItem.addActionListener(e -> anadirEntrada(false));
        modificarGastoItem.addActionListener(e -> modificarGasto());
        cargarExcelItem.addActionListener(e -> cargarDatosExcel());
        guardarExcelItem.addActionListener(e -> guardarDatosExcel());
        salirItem.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "¿Seguro que desea salir?", "Salir", JOptionPane.YES_NO_OPTION);
            if(res == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        actualizarTabla();
    }

    private void anadirEntrada(boolean esGasto) {
        JTextField nombreField = new JTextField();
        JTextField valorField = new JTextField();
        JTextField CantField = new JTextField("1");
        JCheckBox MasVeces = new JCheckBox();

        Object[] message;
        if(esGasto) {
            message = new Object[]{
                    "Nombre", nombreField,
                    "Valor",  valorField,
                    "el gasto se hace mas de una ves?\n",MasVeces
            };
        }
        else{
            CantField.setText("1");
            message = new Object[]{
                    "Nombre", nombreField,
                    "Valor",  valorField
            };
        }
        int op = JOptionPane.showConfirmDialog(null,message, "Añadir entrada", JOptionPane.OK_CANCEL_OPTION);
        if(op == JOptionPane.OK_OPTION&& esGasto && MasVeces.isSelected()) {
                Object[] Mas={
                        "Cuantas veces se hace el Gasto?", CantField
                };
        }
        int option = JOptionPane.showConfirmDialog(this, message, esGasto ? "Añadir Gasto" : "Añadir Ingreso", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            int valor;
            try {
                valor = Integer.parseInt(valorField.getText().trim());
                valor *= Integer.parseInt(CantField.getText().trim());
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (valor < 0) {
                    JOptionPane.showMessageDialog(this, "El valor no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (esGasto) {
                Gasto.gastos.add(new Gasto(nombre, valor));
            } else {
                Gasto.ingresos.add(new Gasto(nombre, valor));
            }
            actualizarTabla();
        }
    }

    private void modificarGasto() {
        if (Gasto.gastos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay gastos para modificar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opciones = new String[Gasto.gastos.size()];
        for (int i = 0; i < Gasto.gastos.size(); i++) {
            opciones[i] = (i + 1) + ": " + Gasto.gastos.get(i);
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione un gasto para modificar:",
                "Modificar Gasto",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            int indice = Integer.parseInt(seleccion.split(":")[0]) - 1;
            Gasto g = Gasto.gastos.get(indice);

            String nuevoValorStr = JOptionPane.showInputDialog(this,
                    "Valor actual: " + g.getValor() + "\nIngrese nuevo valor:");

            if (nuevoValorStr != null) {
                try {
                    int nuevoValor = Integer.parseInt(nuevoValorStr.trim());
                    if (nuevoValor < 0) {
                        JOptionPane.showMessageDialog(this, "El valor no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    g.setValor(nuevoValor);
                    JOptionPane.showMessageDialog(this, "Valor actualizado correctamente.");
                    actualizarTabla();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valor inválido. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0);

        for (Gasto g : Gasto.gastos) {
            tableModel.addRow(new Object[]{"Gasto", g.getName(), g.getValor()});
        }
        for (Gasto g : Gasto.ingresos) {
            tableModel.addRow(new Object[]{"Ingreso", g.getName(), g.getValor()});
        }

        // Actualizar resumen
        int totalGastos = Gasto.gastos.stream().mapToInt(Gasto::getValor).sum();
        int totalIngresos = Gasto.ingresos.stream().mapToInt(Gasto::getValor).sum();
        int saldo = totalIngresos - totalGastos;

        labelTotalGastos.setText("Total Gastos: " + totalGastos);
        labelTotalIngresos.setText("Total Ingresos: " + totalIngresos);
        labelSaldo.setText("Saldo (Ingresos - Gastos): " + saldo);
    }

    private void guardarDatosExcel() {
        String mes = JOptionPane.showInputDialog(this, "Ingrese el mes para guardar los datos:");
        if(mes == null || mes.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mes inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mes = mes.trim();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Gastos e Ingresos");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tipo");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Valor");

        int rowNum = 1;
        for (Gasto g : Gasto.gastos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Gasto");
            row.createCell(1).setCellValue(g.getName());
            row.createCell(2).setCellValue(g.getValor());
        }
        for (Gasto g : Gasto.ingresos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Ingreso");
            row.createCell(1).setCellValue(g.getName());
            row.createCell(2).setCellValue(g.getValor());
        }

        // Fila resumen
        rowNum++;
        int totalGastos = Gasto.gastos.stream().mapToInt(Gasto::getValor).sum();
        int totalIngresos = Gasto.ingresos.stream().mapToInt(Gasto::getValor).sum();
        int saldo = totalIngresos - totalGastos;

        Row totalGastosRow = sheet.createRow(rowNum++);
        totalGastosRow.createCell(0).setCellValue("Resumen");
        totalGastosRow.createCell(1).setCellValue("Total Gastos: ");
        totalGastosRow.createCell(2).setCellValue(totalGastos);

        Row totalIngresosRow = sheet.createRow(rowNum++);
        totalIngresosRow.createCell(0).setCellValue("Resumen");
        totalIngresosRow.createCell(1).setCellValue("Total Ingresos: ");
        totalIngresosRow.createCell(2).setCellValue(totalIngresos);

        Row saldoRow = sheet.createRow(rowNum++);
        saldoRow.createCell(0).setCellValue("Resumen");
        saldoRow.createCell(1).setCellValue("Saldo: ");
        saldoRow.createCell(2).setCellValue(saldo);

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream("gastos_ingresos-" + mes + ".xlsx")) {
            workbook.write(fileOut);
            JOptionPane.showMessageDialog(this, "Datos guardados en 'gastos_ingresos-" + mes + ".xlsx'.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarDatosExcel() {
        String mes = JOptionPane.showInputDialog(this, "Ingrese el mes para cargar los datos:");
        if(mes == null || mes.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mes inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mes = mes.trim();

        try (FileInputStream fis = new FileInputStream("gastos_ingresos-" + mes + ".xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Gasto.gastos.clear();
            Gasto.ingresos.clear();

            Iterator<Row> rowIterator = sheet.iterator();
            boolean firstRow = true;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                Cell tipoCell = row.getCell(0);
                Cell nombreCell = row.getCell(1);
                Cell valorCell = row.getCell(2);

                if (tipoCell == null || nombreCell == null || valorCell == null)
                    continue;

                String tipo = tipoCell.getStringCellValue();
                String nombre = nombreCell.getStringCellValue();
                int valor = 0;
                if (valorCell.getCellType() == CellType.NUMERIC) {
                    valor = (int) valorCell.getNumericCellValue();
                } else if (valorCell.getCellType() == CellType.STRING) {
                    try {
                        valor = Integer.parseInt(valorCell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Valor inválido para " + nombre + ", se omite.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }

                if (tipo.equalsIgnoreCase("Gasto")) {
                    Gasto.gastos.add(new Gasto(nombre, valor));
                } else if (tipo.equalsIgnoreCase("Ingreso")) {
                    Gasto.ingresos.add(new Gasto(nombre, valor));
                }
            }

            JOptionPane.showMessageDialog(this, "Datos cargados correctamente desde el archivo.");
            actualizarTabla();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Establecer look and feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
