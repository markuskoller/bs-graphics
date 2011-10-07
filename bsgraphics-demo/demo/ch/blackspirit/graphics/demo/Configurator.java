/*
 * Copyright 2008 Markus Koller
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.blackspirit.graphics.demo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import ch.blackspirit.graphics.CanvasFactory;
import ch.blackspirit.graphics.DisplayMode;

/**
 * Not yet finished !
 * @author Markus Koller
 */
public class Configurator {
	private JDialog dialog;
	private JComboBox displayModeBox;
	private DefaultComboBoxModel displayModeModel = new DefaultComboBoxModel();
	private DisplayModeComparator displayModeComparator = new DisplayModeComparator();
	
	private DefaultComboBoxModel windowSizeModel = new DefaultComboBoxModel();
	private JComboBox windowSizeBox;
	
	public Configurator(CanvasFactory canvasFactory) {
		
		this.dialog = new JDialog();

		GridBagLayout layout = new GridBagLayout();
		dialog.getContentPane().setLayout(layout);
		
		displayModeBox = new JComboBox();
		displayModeBox.setModel(displayModeModel);
		ArrayList<DisplayMode> displayModes = new ArrayList<DisplayMode>();
		for(DisplayMode displayMode: canvasFactory.getDisplayModes()) {
			displayModes.add(displayMode);
		}
		Collections.sort(displayModes, displayModeComparator);
		for(DisplayMode displayMode: displayModes) {
			displayModeModel.addElement(new DisplayModeItem(displayMode));
		}
		
		GridBagConstraints displayModeBoxConst = new GridBagConstraints();
		displayModeBoxConst.gridx = 0;
		displayModeBoxConst.gridy = 0;
		displayModeBoxConst.insets = new Insets(2, 2, 2, 2);
		dialog.getContentPane().add(displayModeBox, displayModeBoxConst);
		
		JButton okButton = new JButton("Fullscreen");
		
		GridBagConstraints okButtonConst = new GridBagConstraints();
		okButtonConst.gridx = 1;
		okButtonConst.gridy = 0;
		okButtonConst.insets = new Insets(2, 2, 2, 2);
		dialog.getContentPane().add(okButton, okButtonConst);
		
		windowSizeBox = new JComboBox();
		windowSizeBox.setModel(windowSizeModel);
		
		GridBagConstraints windowSizeBoxConst = new GridBagConstraints();
		windowSizeBoxConst.gridx = 0;
		windowSizeBoxConst.gridy = 1;
		windowSizeBoxConst.insets = new Insets(2, 2, 2, 2);
		dialog.getContentPane().add(windowSizeBox, windowSizeBoxConst);

		dialog.pack();
	}
	
	public void show() {
		dialog.setVisible(true);
	}
	
	public void addWindowSize(int width, int height) {
		windowSizeModel.addElement(new DimensionWrapper(width, height));
	}
	
	public static class DimensionWrapper {
		private int width;
		private int height;
		public DimensionWrapper(int width, int height) {
			this.width = width;
			this.height = height;
		}
		public int getHeight() {
			return height;
		}
		public int getWidth() {
			return width;
		}
		public String toString() {
			return width + "x" + height;
		}
	}
	
	private static class DisplayModeItem {
		private DisplayMode displayMode;

		public DisplayModeItem(DisplayMode displayMode) {
			super();
			this.displayMode = displayMode;
		}
		public String toString() {
			return displayMode.getWidth() + "x" + displayMode.getHeight() + ", " + displayMode.getColorDepth() + " bit colour, " + displayMode.getRefreshRate() + " Hz";
		}
	}
	private static class DisplayModeComparator implements Comparator<DisplayMode> {
		public int compare(DisplayMode o1, DisplayMode o2) {
			if(o1.getWidth() < o2.getWidth()) return -1;
			else if(o1.getWidth() > o2.getWidth()) return 1;
			else {
				if(o1.getHeight() < o2.getHeight()) return -1;
				else if(o1.getHeight() > o2.getHeight()) return 1;
				else {
					if(o1.getColorDepth() < o2.getColorDepth()) return -1;
					else if(o1.getColorDepth() > o2.getColorDepth()) return 1;
					else {
						if(o1.getRefreshRate() < o2.getRefreshRate()) return -1;
						else if(o1.getRefreshRate() > o2.getRefreshRate()) return 1;
						else return 0;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Configurator configurator = new Configurator(new ch.blackspirit.graphics.jogl.CanvasFactory());
		configurator.addWindowSize(800, 600);
		configurator.show();
	}
}
