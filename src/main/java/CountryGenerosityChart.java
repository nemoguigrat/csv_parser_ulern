import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Map;

public class CountryGenerosityChart extends JFrame{
    public CountryGenerosityChart(Map<String, Float> data){
        initUI(data);
    }

    private void initUI(Map<String, Float> data) {

        SlidingCategoryDataset dataset = createDataset(data);

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = createChartPanel(chart);
        JScrollBar scroller = createScrollBar(dataset);
        JPanel scrollPanel = createScrollPanell(scroller);

        add(chartPanel);
        add(scrollPanel, BorderLayout.SOUTH);
        pack();
        setTitle("Generosity chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private SlidingCategoryDataset createDataset(Map<String, Float> data){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String key : data.keySet())
            dataset.setValue(data.get(key), "Generosity", key);
        return new SlidingCategoryDataset(dataset,0, 10);
    }

    private JFreeChart createChart(SlidingCategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Generosity country",
                "",
                "Generosity",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JScrollBar createScrollBar(SlidingCategoryDataset dataset){
        int maxSize = dataset.getUnderlyingDataset().getColumnCount();
        JScrollBar scroller =  new JScrollBar(SwingConstants.HORIZONTAL, 0, 10, 0, maxSize);
        scroller.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                dataset.setFirstCategoryIndex(scroller.getValue());
            }
        });
        return scroller;
    }

    private ChartPanel createChartPanel(JFreeChart chart){
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        return chartPanel;
    }

    private JPanel createScrollPanell(JScrollBar scroller){
        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.add(scroller);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        scrollPanel.setBackground(Color.white);
        return scrollPanel;
    }
}
