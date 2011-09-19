package start;

import java.util.ArrayList;
import java.util.List;

import gui.BarChartTab;
import gui.ChartContainersTab;

import com.vaadin.Application;
import com.vaadin.charts.*;
import com.vaadin.charts.BarChart.Orientation;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import domain.ChartItemContainer;

public class ChartappwithownwidgetsetApplication extends Application {

	private static final long serialVersionUID = 3L;

	private static final String[] colors = new String[] { "red", "green", "black", "blue", "yellow", "white" };

	TabSheet tabSheet = new TabSheet();

	private List<ChartItemContainer> containers;
	
	@Override
	public void init() {
		Window mainWindow = new Window("Charts Application");
		setMainWindow(mainWindow);
		mainWindow.addComponent(tabSheet);
		
		containers = new ArrayList<ChartItemContainer>();
		tabSheet.addTab(new ChartContainersTab(containers), "Chart Containers", null);
		tabSheet.addTab(new BarChartTab(colors, containers), "Bar chart", null);
		
	}



}
