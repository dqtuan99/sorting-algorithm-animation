/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barcharttest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author Do Quang Tuan
 */
public class FXMLDocumentController implements Initializable {

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private ComboBox<String> comboBox2;

	@FXML
	private Slider slider;

	@FXML
	private TextField textField;

	@FXML
	private Button button;

	@FXML
	private Button button2;

	@FXML
	private BarChart<String, Number> chart;

	private ArrayList<XYChart.Data<String, Number>> bars;

	private static final String COLOR_ACTIVE = "-fx-bar-fill: ff0000",
			COLOR_ACTIVE_2 = "-fx-bar-fill: #00cdcd",
			COLOR_INITIAL = "-fx-bar-fill: #ffffff",
			COLOR_SORTED = "-fx-bar-fill: #008000",
			COLOR_COMPARING = "-fx-bar-fill: #ffff00";

	static int BAR_COUNT = 512;

	static int INIT_VALUE = 5;

	ObservableList<String> list = FXCollections.observableArrayList(
			"Bubble sort", "Selection sort", "Insertion sort", "Cocktail sort",
			"Pancake sort", "Merge sort", "Shell sort", "Heap sort", "Quick sort",
			"Comb sort", "Tim sort", "Bitonic sort", "Radix sort", "Counting sort"
	);

	ObservableList<String> list2 = FXCollections.observableArrayList(
			"Random array", "Few unique array", "Almost sorted array", "Reversed array"
	);

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		comboBox.setItems(list);
		comboBox.setValue("Bubble sort");
		comboBox2.setItems(list2);
		comboBox2.setValue("Random array");

		slider.setValue(INIT_VALUE);
		slider.valueProperty().addListener((obs, oldval, newVal)
				-> slider.setValue(Math.round(newVal.doubleValue())));

		textField.setText(String.valueOf(INIT_VALUE));
		textField.textProperty().bindBidirectional(slider.valueProperty(), new NumberStringConverter());

		button.setOnAction(event -> new Thread(() -> {
			if (comboBox2.getValue() != null) {
				disableUI(true);

				if (comboBox.getValue() == "Bubble sort") {
					bubbleSort();
				} else if (comboBox.getValue() == "Selection sort") {
					selectionSort();
				} else if (comboBox.getValue() == "Insertion sort") {
					insertionSort();
				} else if (comboBox.getValue() == "Merge sort") {
					mergeSort(0, BAR_COUNT - 1);
				} else if (comboBox.getValue() == "Shell sort") {
					shellSort();
				} else if (comboBox.getValue() == "Quick sort") {
					quickSort(0, BAR_COUNT - 1);
				} else if (comboBox.getValue() == "Heap sort") {
					heapSort();
				} else if (comboBox.getValue() == "Counting sort") {
					countingSort();
				} else if (comboBox.getValue() == "Radix sort") {
					radixSort();
				} else if (comboBox.getValue() == "Comb sort") {
					combSort();
				} else if (comboBox.getValue() == "Cocktail sort") {
					cocktailSort();
				} else if (comboBox.getValue() == "Pancake sort") {
					pancakeSort();
				} else if (comboBox.getValue() == "Bitonic sort") {
					bitonicSort();
				} else if (comboBox.getValue() == "Tim sort") {
					timSort();
				}

				for (int i = 0; i < BAR_COUNT; ++i) {
					int delay = 700 / BAR_COUNT;
					if (delay == 0) {
						delay = 1;
					}
					paint(i, COLOR_SORTED);
					delay(delay);
				}

				disableUI(false);
			}
		}).start());

		refreshBarChart();

		Platform.runLater(() -> randomArray());
	}

	//Event handler
	//--------------------------------------------------------------------------
	public void refreshBarChart() {
		bars = new ArrayList<XYChart.Data<String, Number>>();
		final XYChart.Series<String, Number> data = new XYChart.Series<>();
		chart.getData().add(data);
		for (int i = 0; i < BAR_COUNT; ++i) {
			bars.add(new XYChart.Data<>(i + "", i + 1));
			data.getData().add(bars.get(i));
			paint(i, COLOR_INITIAL);
		}
	}

	public void handleComboBoxAction(ActionEvent event) {
		if (comboBox2.getValue() == "Random array") {
			Platform.runLater(() -> randomArray());
		} else if (comboBox2.getValue() == "Few unique array") {
			Platform.runLater(() -> fewUniqueArray());
		} else if (comboBox2.getValue() == "Almost sorted array") {
			Platform.runLater(() -> almostSortedArray());
		} else if (comboBox2.getValue() == "Reversed array") {
			Platform.runLater(() -> reversedArray());
		}
	}

	public void handleGenerateAction(ActionEvent event) {
		if (comboBox2.getValue() == "Random array") {
			Platform.runLater(() -> randomArray());
		} else if (comboBox2.getValue() == "Few unique array") {
			Platform.runLater(() -> fewUniqueArray());
		} else if (comboBox2.getValue() == "Almost sorted array") {
			Platform.runLater(() -> almostSortedArray());
		} else if (comboBox2.getValue() == "Reversed array") {
			Platform.runLater(() -> reversedArray());
		}

		for (int i = 0; i < BAR_COUNT; ++i) {
			paint(i, COLOR_INITIAL);
		}
	}

	public void disableUI(boolean isDisable) {
		button.setDisable(isDisable);
		button2.setDisable(isDisable);
		slider.setDisable(isDisable);
		textField.setDisable(isDisable);
		comboBox.setDisable(isDisable);
		comboBox2.setDisable(isDisable);
	}

	public void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//--------------------------------------------------------------------------

	//Bar data getter and setter
	//--------------------------------------------------------------------------
	public void setBar(int index, int value) {
		bars.get(index).setYValue(value);
	}

	public int getBar(int index) {
		return (int) bars.get(index).getYValue();
	}
	//--------------------------------------------------------------------------

	//Fill color
	//--------------------------------------------------------------------------
	public void paint(int index, String style) {
		Platform.runLater(() -> {
			bars.get(index).getNode().setStyle(style);
		});
	}
	//--------------------------------------------------------------------------

	//Array type
	//--------------------------------------------------------------------------
	public void randomArray() {
		Random rd = new Random();

		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, i + 1);
			paint(i, COLOR_INITIAL);
		}

		for (int i = 1; i < BAR_COUNT; ++i) {
			int j = rd.nextInt(i);

			int temp = getBar(i);
			setBar(i, getBar(j));
			setBar(j, temp);
		}
	}

	public void fewUniqueArray() {
		Random rd = new Random();

		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, ((i % 5) + 1) * (BAR_COUNT / 5));
			paint(i, COLOR_INITIAL);
		}

		for (int i = 1; i < BAR_COUNT; ++i) {
			int j = rd.nextInt(i);

			int temp = getBar(i);
			setBar(i, getBar(j));
			setBar(j, temp);
		}
	}

	public void almostSortedArray() {
		randomArray();
		int[] gaps = {701, 301, 132, 57, 23, 10};

		for (int gap : gaps) {
			for (int i = gap; i < BAR_COUNT; ++i) {
				int key = getBar(i);

				int j = i;

				while (j >= gap && key < getBar(j - gap)) {
					int temp = getBar(j);
					setBar(j, getBar(j - gap));
					setBar(j - gap, temp);

					j -= gap;
				}
			}
		}
	}

	public void reversedArray() {
		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, BAR_COUNT - i);
			paint(i, COLOR_INITIAL);
		}
	}
	//--------------------------------------------------------------------------

	//Sorting algorithm
	//--------------------------------------------------------------------------
	public void bubbleSort() {
		for (int i = 0; i < BAR_COUNT - 1; i++) {
			for (int j = i + 1; j < BAR_COUNT; j++) {
				if (getBar(i) > getBar(j)) {
					int temp = getBar(i);
					setBar(i, getBar(j));
					setBar(j, temp);
				}

				paint(i, COLOR_COMPARING);
				paint(j, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(j, COLOR_INITIAL);

			}
		}
	}

	//--------------------------------------------------------------------------
	public void selectionSort() {
		int min_index = 0;
		for (int i = 0; i < BAR_COUNT - 1; ++i) {
			paint(min_index, COLOR_INITIAL);

			min_index = i;

			paint(min_index, COLOR_ACTIVE);

			for (int j = i + 1; j < BAR_COUNT; ++j) {
				if (getBar(j) < getBar(min_index)) {
					paint(min_index, COLOR_INITIAL);

					min_index = j;

					paint(min_index, COLOR_ACTIVE);
					delay((int) slider.getValue());
				} else {
					paint(j, COLOR_COMPARING);
					delay((int) slider.getValue());
					paint(j, COLOR_INITIAL);
				}
			}

			int temp = getBar(i);
			setBar(i, getBar(min_index));
			setBar(min_index, temp);
		}
	}

	//--------------------------------------------------------------------------
	public void insertionSort() {
		for (int i = 1; i < BAR_COUNT; ++i) {
			int key = getBar(i);

			int j = i - 1;

			while (j >= 0 && key < getBar(j)) {
				delay((int) slider.getValue());

				int temp = getBar(j + 1);
				setBar(j + 1, getBar(j));
				setBar(j, temp);

				paint(j + 1, COLOR_INITIAL);
				paint(j, COLOR_ACTIVE);
				delay((int) slider.getValue());
				paint(j, COLOR_INITIAL);

				--j;
			}
		}
	}

	//--------------------------------------------------------------------------
	void cocktailSort() {
		boolean swapped = true;
		int start = 0;
		int end = BAR_COUNT - 1;

		while (swapped) {
			swapped = false;

			for (int i = start; i < end; ++i) {
				if (getBar(i) > getBar(i + 1)) {
					int temp = getBar(i);
					setBar(i, getBar(i + 1));
					setBar(i + 1, temp);

					swapped = true;
				}

				paint(i, COLOR_COMPARING);
				paint(i + 1, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(i + 1, COLOR_INITIAL);
			}

			if (!swapped) {
				break;
			}

			swapped = false;

			--end;

			for (int i = end - 1; i >= start; --i) {
				if (getBar(i) > getBar(i + 1)) {
					int temp = getBar(i);
					setBar(i, getBar(i + 1));
					setBar(i + 1, temp);

					swapped = true;
				}

				paint(i, COLOR_COMPARING);
				paint(i + 1, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(i + 1, COLOR_INITIAL);
			}

			++start;
		}
	}

	//--------------------------------------------------------------------------
	public void flip(int i) {
		int temp, start = 0;
		while (start < i) {
			temp = getBar(start);
			setBar(start, getBar(i));
			setBar(i, temp);

			paint(start, COLOR_COMPARING);
			paint(i, COLOR_COMPARING);
			delay((int) slider.getValue());
			paint(start, COLOR_INITIAL);
			paint(i, COLOR_INITIAL);

			++start;
			--i;
		}
	}

	public int findMax(int n) {
		int mi, i;
		for (mi = 0, i = 0; i < n; ++i) {
			if (getBar(i) > getBar(mi)) {
				mi = i;
			}
		}
		return mi;
	}

	public void pancakeSort() {
		for (int curr_size = BAR_COUNT; curr_size > 1; --curr_size) {
			int mi = findMax(curr_size);

			if (mi != curr_size - 1) {
				flip(mi);

				flip(curr_size - 1);
			}
		}
	}

	//--------------------------------------------------------------------------
	public void merge(int l, int m, int r) {
		int n1 = m - l + 1;
		int n2 = r - m;

		int L[] = new int[n1];
		int R[] = new int[n2];

		for (int i = 0; i < n1; ++i) {
			L[i] = getBar(l + i);
		}

		for (int j = 0; j < n2; ++j) {
			R[j] = getBar(m + 1 + j);
		}

		int i = 0, j = 0;
		int k = l;

		while (i < n1 && j < n2) {
			paint(l + i, COLOR_COMPARING);
			paint(m + 1 + j, COLOR_COMPARING);
			delay((int) slider.getValue());
			paint(l + i, COLOR_INITIAL);
			paint(m + 1 + j, COLOR_INITIAL);

			if (L[i] <= R[j]) {
				setBar(k, L[i]);
				++i;
			} else {
				setBar(k, R[j]);
				++j;
			}
			++k;

		}

		while (i < n1) {
			setBar(k, L[i]);
			i++;
			k++;
			delay((int) slider.getValue());
		}

		while (j < n2) {
			setBar(k, R[j]);
			j++;
			k++;
			delay((int) slider.getValue());
		}
	}

	public void mergeSort(int l, int r) {
		if (l < r) {
			int m = (l + r) / 2;
			mergeSort(l, m);
			mergeSort(m + 1, r);
			merge(l, m, r);
		}
	}

	//--------------------------------------------------------------------------
	public void shellSort() {
		int[] gaps = {701, 301, 132, 57, 23, 10, 4, 1};

		for (int gap : gaps) {
			for (int i = gap; i < BAR_COUNT; ++i) {
				int key = getBar(i);

				int j = i;

				paint(j, COLOR_COMPARING);
				paint(j - gap, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(j, COLOR_INITIAL);
				paint(j - gap, COLOR_INITIAL);

				while (j >= gap && key < getBar(j - gap)) {
					int temp = getBar(j);
					setBar(j, getBar(j - gap));
					setBar(j - gap, temp);

					paint(j, COLOR_COMPARING);
					paint(j - gap, COLOR_COMPARING);
					delay((int) slider.getValue());
					paint(j, COLOR_INITIAL);
					paint(j - gap, COLOR_INITIAL);

					j -= gap;
				}
			}
		}
	}

	//--------------------------------------------------------------------------
	public void quickSort(int left, int right) {
		int middle = left + (right - left) / 2;
		int pivot = getBar(middle);
		int i = left, j = right;

		while (i <= j) {

			while (getBar(i) < pivot) {
				++i;

				paint(i, COLOR_COMPARING);
				paint(j, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(j, COLOR_INITIAL);
			}

			while (getBar(j) > pivot) {
				--j;

				paint(i, COLOR_COMPARING);
				paint(j, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(j, COLOR_INITIAL);
			}

			if (i <= j) {
				int temp = getBar(i);
				setBar(i, getBar(j));
				setBar(j, temp);

				++i;
				--j;

				paint(i, COLOR_COMPARING);
				paint(j, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(j, COLOR_INITIAL);
			}
		}

		if (left < j) {
			paint(j + 1, COLOR_ACTIVE);
			quickSort(left, j);
		}

		if (right > i) {
			paint(i - 1, COLOR_ACTIVE_2);
			quickSort(i, right);
		}

		paint(left - 1, COLOR_INITIAL);
		paint(right + 1, COLOR_INITIAL);
	}

	//--------------------------------------------------------------------------
	public void heapify(int i, int n) {
		int left = 2 * i + 1;
		int right = 2 * i + 2;

		int max = i;

		if ((left < n) && (getBar(left) > getBar(max))) {
			max = left;
		}

		if ((right < n) && (getBar(right) > getBar(max))) {
			max = right;
		}

		if (max != i) {
			paint(i, COLOR_COMPARING);
			paint(max, COLOR_COMPARING);
			delay((int) slider.getValue());
			paint(i, COLOR_INITIAL);
			paint(max, COLOR_INITIAL);

			int temp = getBar(i);
			setBar(i, getBar(max));
			setBar(max, temp);

			heapify(max, n);
		}
	}

	public void heapSort() {

		for (int i = BAR_COUNT / 2 - 1; i >= 0; --i) {
			heapify(i, BAR_COUNT);
		}

		for (int i = BAR_COUNT - 1; i >= 0; --i) {
			int temp = getBar(0);
			setBar(0, getBar(i));
			setBar(i, temp);

			heapify(0, i);
		}
	}

	//--------------------------------------------------------------------------	
	int getNextGap(int gap) {
		gap = (gap * 10) / 13;

		if (gap < 1) {
			return 1;
		}
		return gap;
	}

	void combSort() {
		int gap = BAR_COUNT;

		boolean swapped = true;

		while (gap != 1 || swapped == true) {
			gap = getNextGap(gap);

			swapped = false;

			for (int i = 0; i < BAR_COUNT - gap; ++i) {
				if (getBar(i) > getBar(i + gap)) {
					int temp = getBar(i);
					setBar(i, getBar(i + gap));
					setBar(i + gap, temp);

					swapped = true;
				}

				paint(i, COLOR_COMPARING);
				paint(i + gap, COLOR_COMPARING);
				delay((int) slider.getValue());
				paint(i, COLOR_INITIAL);
				paint(i + gap, COLOR_INITIAL);
			}
		}
	}

	//--------------------------------------------------------------------------
	void compAndSwap(int i, int j, int dir) {
		if ((getBar(i) > getBar(j) && dir == 1)
				|| (getBar(i) < getBar(j) && dir == 0)) {
			int temp = getBar(i);
			setBar(i, getBar(j));
			setBar(j, temp);
		}

		paint(i, COLOR_COMPARING);
		paint(j, COLOR_COMPARING);
		delay((int) slider.getValue());
		paint(i, COLOR_INITIAL);
		paint(j, COLOR_INITIAL);
	}

	void bitonicMerge(int low, int cnt, int dir) {
		if (cnt > 1) {
			int k = cnt / 2;
			for (int i = low; i < low + k; i++) {
				compAndSwap(i, i + k, dir);
			}
			bitonicMerge(low, k, dir);
			bitonicMerge(low + k, k, dir);
		}
	}

	void bitonicSort(int low, int cnt, int dir) {
		if (cnt > 1) {
			int k = cnt / 2;

			bitonicSort(low, k, 1);

			bitonicSort(low + k, k, 0);

			bitonicMerge(low, cnt, dir);
		}
	}

	void bitonicSort() {
		bitonicSort(0, BAR_COUNT, 1);
	}

	//--------------------------------------------------------------------------
	static int RUN = 32;

	public void insertionSort(int left, int right) {
		for (int i = left + 1; i <= right; ++i) {
			int key = getBar(i);

			int j = i - 1;

			while (j >= left && key < getBar(j)) {
				delay((int) slider.getValue());

				int temp = getBar(j + 1);
				setBar(j + 1, getBar(j));
				setBar(j, temp);

				paint(j + 1, COLOR_INITIAL);
				paint(j, COLOR_ACTIVE);
				delay((int) slider.getValue());
				paint(j, COLOR_INITIAL);

				--j;
			}
		}
	}

	void timSort() {
		for (int i = 0; i < BAR_COUNT; i += RUN) {
			insertionSort(i, Math.min((i + 31), (BAR_COUNT - 1)));
		}

		for (int size = RUN; size < BAR_COUNT; size = 2 * size) {
			for (int left = 0; left < BAR_COUNT; left += 2 * size) {
				int mid = left + size - 1;
				int right = Math.min((left + 2 * size - 1), (BAR_COUNT - 1));

				merge(left, mid, right);
			}
		}
	}

	//--------------------------------------------------------------------------
	public void countingSort() {
		int output[] = new int[BAR_COUNT];

		int min = getBar(0);
		int max = getBar(0);
		for (int i = 0; i < BAR_COUNT; i++) {
			if (getBar(i) < min) {
				min = getBar(i);
			} else if (getBar(i) > max) {
				max = getBar(i);
			}
		}

		int count[] = new int[max - min + 1];

		for (int i = 0; i < BAR_COUNT; i++) {
			++count[getBar(i) - min];

			paint(i, COLOR_ACTIVE);
			delay((int) slider.getValue());
			paint(i, COLOR_INITIAL);
		}

		for (int i = 1; i < count.length; i++) {
			count[i] += count[i - 1];
		}

		for (int i = BAR_COUNT - 1; i >= 0; i--) {
			paint(i, COLOR_ACTIVE);
			delay((int) slider.getValue());
			paint(i, COLOR_INITIAL);

			output[count[getBar(i) - min] - 1] = getBar(i);
			--count[getBar(i) - min];
		}
		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, output[i]);

			paint(i, COLOR_ACTIVE_2);
			delay((int) slider.getValue());
			paint(i, COLOR_INITIAL);
		}
	}

	//--------------------------------------------------------------------------
	public int getMax() {
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < BAR_COUNT; ++i) {
			if (getBar(i) > max) {
				max = getBar(i);
			}
		}

		return max;
	}

	void countingSort2(int exp) {
		int output[] = new int[BAR_COUNT];
		int count[] = new int[10];
		Arrays.fill(count, 0);

		for (int i = 0; i < BAR_COUNT; i++) {
			++count[(getBar(i) / exp) % 10];

			paint(i, COLOR_ACTIVE);
			delay((int) slider.getValue());
			paint(i, COLOR_INITIAL);
		}

		for (int i = 1; i < 10; i++) {
			count[i] += count[i - 1];
		}

		for (int i = BAR_COUNT - 1; i >= 0; i--) {
			output[count[(getBar(i) / exp) % 10] - 1] = getBar(i);
			--count[(getBar(i) / exp) % 10];
		}

		for (int i = 0; i < BAR_COUNT; i++) {
			setBar(i, output[i]);

			paint(i, COLOR_ACTIVE_2);
			delay((int) slider.getValue());
			paint(i, COLOR_INITIAL);
		}
	}

	public void radixSort() {
		int max = getMax();

		for (int exp = 1; max / exp > 0; exp *= 10) {
			countingSort2(exp);
		}
	}
	//--------------------------------------------------------------------------
}
