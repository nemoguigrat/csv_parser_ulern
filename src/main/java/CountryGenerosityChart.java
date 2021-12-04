import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CountryGenerosityChart extends JFrame{
    public CountryGenerosityChart(Map<String, Float> data){
        initUI(data);
    }

    private void initUI(Map<String, Float> data) {

        CategoryDataset dataset = createDataset(data);

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);

        add(chartPanel);

        pack();
        setTitle("Bar chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset(Map<String, Float> data){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String key : data.keySet())
            dataset.setValue(data.get(key), "Generosity", key);
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset)
    {
        JFreeChart barChart = ChartFactory.createBarChart(
                "Generosity country",
                "",
                "Generosity",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        return barChart;
    }
}
