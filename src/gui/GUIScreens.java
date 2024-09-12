package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import dmarm.utilities.AugmentationObject;
import dmarm.utilities.ExtractionFunctions;
import dmarm.utilities.AugmentationFunctions;
import dmarm.utilities.Functions;
import dmarm.utilities.ResultsObject;

public class GUIScreens {

	protected static void mainMenuScreen(JFrame guiFrame) {
		guiFrame.setTitle("Distributed Multi-level Association Rule Mining");
		guiFrame.setLocationRelativeTo(null);

		final JPanel mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 640, 480);
		guiFrame.getContentPane().setLayout(null);
		guiFrame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);

		JButton augmentFilesButton = new JButton("Augment Data Files");
		augmentFilesButton.setLocation(186, 146);
		mainPanel.add(augmentFilesButton);
		augmentFilesButton.setSize(228, 40);
		augmentFilesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				guiFrame.setVisible(false);
				guiFrame.getContentPane().remove(mainPanel);
				setupScreen(guiFrame, new AugmentationObject());
			}
		});

		JButton extractRulesButton = new JButton("Extract Rules From File");
		extractRulesButton.setLocation(186, 197);
		mainPanel.add(extractRulesButton);
		extractRulesButton.setSize(228, 40);
		extractRulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				guiFrame.setVisible(false);
				guiFrame.remove(mainPanel);
				extractionScreen(guiFrame, new ResultsObject());
			}
		});

		JButton exitButton = new JButton("Exit");
		exitButton.setLocation(250, 248);
		mainPanel.add(exitButton);
		exitButton.setSize(116, 25);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		JLabel versionLabel = new JLabel("Version 1.0");
		versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		versionLabel.setBounds(493, 404, 109, 40);
		mainPanel.add(versionLabel);

		guiFrame.setVisible(true);
		guiFrame.revalidate();
	}

	protected static void setupScreen(JFrame guiFrame, AugmentationObject augmObj) {

		guiFrame.setTitle("Setup Screen");
		guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(), BoxLayout.X_AXIS));

		JPanel setupPanel = new JPanel();
		guiFrame.getContentPane().add(setupPanel);
		setupPanel.setBounds(guiFrame.getBounds());
		setupPanel.setLayout(null);

		JLabel inputDirLabel = new JLabel("Input Directory");
		inputDirLabel.setBounds(27, 90, 125, 30);
		setupPanel.add(inputDirLabel);

		JTextField inputDir = new JTextField();
		inputDir.setBounds(165, 90, 320, 20);
		setupPanel.add(inputDir);
		inputDir.setColumns(10);
		inputDir.setEditable(true);

		JLabel outputDirLabel = new JLabel("Output Directory");
		outputDirLabel.setBounds(27, 140, 125, 30);
		setupPanel.add(outputDirLabel);

		JTextField outputDir = new JTextField();
		outputDir.setBounds(165, 140, 320, 20);
		setupPanel.add(outputDir);
		outputDir.setColumns(10);
		outputDir.setEditable(true);

		JLabel hierarchiesDirLabel = new JLabel("Hierarchies Directory");
		hierarchiesDirLabel.setBounds(27, 190, 125, 30);
		setupPanel.add(hierarchiesDirLabel);

		JTextField hierarchiesDir = new JTextField();
		hierarchiesDir.setBounds(165, 190, 320, 20);
		setupPanel.add(hierarchiesDir);
		hierarchiesDir.setColumns(10);
		hierarchiesDir.setEditable(true);

		JLabel mappingFileLabel = new JLabel("Mapping File");
		mappingFileLabel.setBounds(27, 240, 125, 30);
		setupPanel.add(mappingFileLabel);

		JTextField mappingFile = new JTextField();
		mappingFile.setBounds(165, 240, 320, 20);
		setupPanel.add(mappingFile);
		mappingFile.setColumns(10);
		mappingFile.setEditable(true);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 374, 640, 2);
		setupPanel.add(separator);

		JButton backButton = new JButton("Back");
		backButton.setBounds(27, 387, 90, 30);
		setupPanel.add(backButton);
		backButton.setEnabled(true);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				guiFrame.getContentPane().remove(setupPanel);
				guiFrame.setVisible(false);
				mainMenuScreen(guiFrame);
			}
		});

		JButton importButton = new JButton("Import");
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				if (fileChooser.showOpenDialog(importPanel)==0) {
					AugmentationFunctions.updatePathsFromFile(augmObj, fileChooser.getSelectedFile().toString());
					inputDir.setText(augmObj.getInputFolder());
					outputDir.setText(augmObj.getOutputFolder());
					hierarchiesDir.setText(augmObj.getHierarchyFilesFolder());
					mappingFile.setText(augmObj.getMappingFile());
					JOptionPane.showMessageDialog(guiFrame,
							"Successfully imported configuration file -> " + fileChooser.getSelectedFile().toString());
				}
			}
		});
			
		importButton.setBounds(189, 387, 90, 30);
		setupPanel.add(importButton);

		JButton nextButton = new JButton("Next");
		nextButton.setBounds(524, 387, 90, 30);
		setupPanel.add(nextButton);
		nextButton.setEnabled(true);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				boolean result;
				result = (!Functions.isEmptyOrNullString(inputDir.getText().trim())
						&& !Functions.isEmptyOrNullString(outputDir.getText())
						&& !Functions.isEmptyOrNullString(hierarchiesDir.getText())
						&& !Functions.isEmptyOrNullString(mappingFile.getText()));

				if (result) {
					augmObj.setInputFolder(inputDir.getText().trim());
					augmObj.setOutputFolder(outputDir.getText().trim());
					augmObj.setHierarchyFilesFolder(hierarchiesDir.getText().trim());
					augmObj.setMappingFile(mappingFile.getText().trim());
					guiFrame.getContentPane().remove(setupPanel);
					guiFrame.setVisible(false);
					augmentationScreen(guiFrame, augmObj);
				} else {
					JOptionPane.showMessageDialog(guiFrame, "Some paths have not been set. Cannot proceed.");
				}
			}
		});

		JButton helpButton = new JButton("Help");
		helpButton.setBounds(368, 387, 90, 30);
		setupPanel.add(helpButton);
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				
				/* to be implemented, change the help text */
				JOptionPane.showMessageDialog(guiFrame,
						"Instructions follow:" + "\nChoose a BasePath to enable other paths and proceeding"
								+ "\nOther paths are set at default values, but may be edited"
								+ "\nThe save button will save the current configuration to an xml file"
								+ "\nThe next button will lead to the execution screen");
			}
		});

		JButton inputDirChooser = new JButton("...");
		inputDirChooser.setBounds(541, 90, 31, 20);
		setupPanel.add(inputDirChooser);
		inputDirChooser.setEnabled(true);
		inputDirChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					inputDir.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JButton outputDirChooser = new JButton("...");
		outputDirChooser.setBounds(541, 140, 31, 20);
		setupPanel.add(outputDirChooser);
		outputDirChooser.setEnabled(true);
		outputDirChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					outputDir.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JButton hierarchiesDirChooser = new JButton("...");
		hierarchiesDirChooser.setBounds(541, 190, 31, 20);
		setupPanel.add(hierarchiesDirChooser);
		hierarchiesDirChooser.setEnabled(true);
		hierarchiesDirChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					hierarchiesDir.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JButton mappingFileChooser = new JButton("...");
		mappingFileChooser.setBounds(541, 240, 31, 20);
		setupPanel.add(mappingFileChooser);
		mappingFileChooser.setEnabled(true);
		mappingFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					mappingFile.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JLabel setupLabel = new JLabel("Setup Screen");
		setupLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		setupLabel.setHorizontalAlignment(SwingConstants.CENTER);
		setupLabel.setBounds(207, 11, 238, 30);
		setupPanel.add(setupLabel);

		guiFrame.setVisible(true);
		guiFrame.revalidate();
	}

	protected static void augmentationScreen(JFrame guiFrame, AugmentationObject augmObj) {

		guiFrame.setTitle("Augmentation Screen");
		JPanel augmentationPanel = new JPanel();
		guiFrame.getContentPane().add(augmentationPanel);
		augmentationPanel.setBounds(guiFrame.getBounds());
		augmentationPanel.setLayout(null);

		JTextArea loggerArea = new JTextArea();
		loggerArea.setLineWrap(true);
		loggerArea.setBounds(10, 35, 266, 215);
		loggerArea.setEditable(false);
		loggerArea.setVisible(true);
		loggerArea.setText("");

		JScrollPane loggerScroll = new JScrollPane(loggerArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		loggerScroll.setBounds(10, 35, 416, 339);
		loggerScroll.setEnabled(true);
		augmentationPanel.add(loggerScroll);

		JLabel logLabel = new JLabel("Log");
		logLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		logLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logLabel.setBounds(10, 0, 380, 27);
		augmentationPanel.add(logLabel);

		JLabel inputFilesLabel = new JLabel("Input Files Found: ");
		inputFilesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputFilesLabel.setBounds(436, 35, 194, 30);
		augmentationPanel.add(inputFilesLabel);
		inputFilesLabel.setEnabled(true);

		JLabel hierarchyFilesLabel = new JLabel("Hierarchy Files Found: ");
		hierarchyFilesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hierarchyFilesLabel.setBounds(436, 76, 194, 30);
		augmentationPanel.add(hierarchyFilesLabel);
		hierarchyFilesLabel.setEnabled(true);

		JLabel mappingsLabel = new JLabel("Mappings Found: ");
		mappingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mappingsLabel.setBounds(436, 117, 194, 30);
		augmentationPanel.add(mappingsLabel);
		mappingsLabel.setEnabled(true);

		JButton backButton = new JButton("Back");
		backButton.setBounds(122, 402, 130, 30);
		augmentationPanel.add(backButton);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				guiFrame.remove(augmentationPanel);
				guiFrame.setVisible(false);
				setupScreen(guiFrame, null);
			}
		});

		JButton saveConfigButton = new JButton("Save Configuration");
		saveConfigButton.setBounds(436, 241, 194, 30);
		augmentationPanel.add(saveConfigButton);
		saveConfigButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean result;
				loggerArea.setText(loggerArea.getText() + "Generating configuration file...\n");
				result = dmarm.utilities.ConfigurationGenerator.generateConfigurationFile(augmObj);
				if (result) {
					JOptionPane.showMessageDialog(guiFrame,
							"Configuration export successful at -> " + augmObj.getOutputFolder() + "\\ConfigurationFile.xml!");
					loggerArea.setText(loggerArea.getText() + "Configuration export successful at -> " + augmObj.getOutputFolder() + "\\ConfigurationFile.xml!\n");
				} else {
					JOptionPane.showMessageDialog(guiFrame,
							"An error occured while creating -> " + augmObj.getOutputFolder() + "\\ConfigurationFile.xml!");
					loggerArea.setText(loggerArea.getText() + "An error occured while creating -> " + augmObj.getOutputFolder() + "\\ConfigurationFile.xml!\n");
				}
			}
		});

		JButton createButton = new JButton("Create File");
		createButton.setBounds(393, 402, 130, 30);
		augmentationPanel.add(createButton);
		createButton.setEnabled(false);
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				boolean result;
				loggerArea.setText(loggerArea.getText() + "Data augmentation began...\n");
				result = AugmentationFunctions.augmentationFunction(augmObj);
				if (result) {
					JOptionPane.showMessageDialog(guiFrame,
							"Created successfully -> " + augmObj.getOutputFolder() + "\\AugmentedDataFile.txt");
					loggerArea.setText(loggerArea.getText() + loggerArea.getText() + "Configuration export successful at -> " + augmObj.getOutputFolder() + "\\AugmentedDataFile.txt\n");
					/* If the augmented file was created successfully, go back to Main Menu */
					guiFrame.remove(augmentationPanel);
					guiFrame.setVisible(false);
					mainMenuScreen(guiFrame);
				} else {
					JOptionPane.showMessageDialog(guiFrame,
							"An error occured while creating ->" + augmObj.getOutputFolder() + "\\AugmentedDataFile.txt");
					loggerArea.setText(loggerArea.getText() + loggerArea.getText() + "An error occured while creating -> " + augmObj.getOutputFolder() + "\\AugmentedDataFile.txt\n");
				}
			}
		});

		JButton reviewConfigButton = new JButton("Review Configuration");
		reviewConfigButton.setBounds(436, 200, 194, 30);
		augmentationPanel.add(reviewConfigButton);
		reviewConfigButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(guiFrame,
						"Input folder=> " + augmObj.getInputFolder() + "\nOutpout folder=> " + augmObj.getOutputFolder()
								+ "\nHierarchies folder=> " + augmObj.getHierarchyFilesFolder() + "\nMapping file=> "
								+ augmObj.getMappingFile());
			}
		});

		guiFrame.setVisible(true);
		guiFrame.revalidate();
		loggerArea.setText(loggerArea.getText() + "Setup process initiated...\n");
		AugmentationFunctions.setupFunction(augmObj);
		createButton.setEnabled(true);
		loggerArea.setText(loggerArea.getText() + "Setup process completed!\n");
		/* update label fields */
		inputFilesLabel.setText("Input Files Found: " + augmObj.getInputFileNumber());
		loggerArea.setText(loggerArea.getText() + "Input Files Found: " + augmObj.getInputFileNumber() + "\n");
		hierarchyFilesLabel.setText("Hierarchy Files Found: " + augmObj.getHierarchyFileNumber());
		loggerArea.setText(loggerArea.getText() + "Hierarchy Files Found: " + augmObj.getHierarchyFileNumber() + "\n");
		mappingsLabel.setText("Mappings Found: " + augmObj.getMappingsNumber());
		loggerArea.setText(loggerArea.getText() + "Mappings Found: " + augmObj.getMappingsNumber() + "\n");
		loggerArea.setText(loggerArea.getText() + "Standing by...\n");
		guiFrame.setVisible(true);
		guiFrame.revalidate();
	}

	protected static void extractionScreen(JFrame guiFrame, ResultsObject resObj) {

		guiFrame.setTitle("Rule Extraction Screen");
		guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(), BoxLayout.X_AXIS));

		JPanel extractionPanel = new JPanel();
		guiFrame.getContentPane().add(extractionPanel);
		extractionPanel.setBounds(guiFrame.getBounds());
		extractionPanel.setLayout(null);

		JLabel initialFileLabel = new JLabel("Initial Data File");
		initialFileLabel.setBounds(27, 90, 125, 30);
		extractionPanel.add(initialFileLabel);

		JTextField initialFilePath = new JTextField();
		initialFilePath.setBounds(165, 90, 320, 20);
		extractionPanel.add(initialFilePath);
		initialFilePath.setColumns(10);
		initialFilePath.setEditable(true);

		JLabel hadoopFileLabel = new JLabel("Hadoop Results File");
		hadoopFileLabel.setBounds(27, 140, 125, 30);
		extractionPanel.add(hadoopFileLabel);

		JTextField hadoopFilePath = new JTextField();
		hadoopFilePath.setBounds(165, 140, 320, 20);
		extractionPanel.add(hadoopFilePath);
		hadoopFilePath.setColumns(10);
		hadoopFilePath.setEditable(true);

		JLabel outputDirLabel = new JLabel("Output Directory");
		outputDirLabel.setBounds(27, 190, 125, 30);
		extractionPanel.add(outputDirLabel);

		JTextField outputDir = new JTextField();
		outputDir.setBounds(165, 190, 320, 20);
		extractionPanel.add(outputDir);
		outputDir.setColumns(10);
		outputDir.setEditable(true);

		JLabel minSupLabel = new JLabel("Minimum Support");
		minSupLabel.setBounds(27, 281, 125, 30);
		extractionPanel.add(minSupLabel);

		JTextField minSupThreshold = new JTextField();
		minSupThreshold.setBounds(165, 284, 130, 25);
		extractionPanel.add(minSupThreshold);
		minSupThreshold.setColumns(10);
		minSupThreshold.setEditable(true);
		
		JLabel minConfLabel = new JLabel("Minimum Confidence");
		minConfLabel.setBounds(27, 322, 125, 30);
		extractionPanel.add(minConfLabel);

		JTextField minConfThreshold = new JTextField();
		minConfThreshold.setBounds(165, 325, 130, 25);
		extractionPanel.add(minConfThreshold);
		minConfThreshold.setColumns(10);
		minConfThreshold.setEditable(true);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 374, 640, 2);
		extractionPanel.add(separator);

		JButton backButton = new JButton("Back");
		backButton.setBounds(27, 397, 90, 30);
		extractionPanel.add(backButton);
		backButton.setEnabled(true);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				guiFrame.getContentPane().remove(extractionPanel);
				guiFrame.setVisible(false);
				mainMenuScreen(guiFrame);
			}
		});

		JButton nextButton = new JButton("Next");
		nextButton.setBounds(482, 397, 90, 30);
		extractionPanel.add(nextButton);
		nextButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
					guiFrame.getContentPane().remove(extractionPanel);
					guiFrame.setVisible(false);
					rulesScreen(guiFrame, resObj);
			}
		});
		
		JButton extractButton = new JButton("Extract Rules");
		extractButton.setEnabled(true);
		extractButton.setBounds(241, 397, 140, 30);
		extractionPanel.add(extractButton);
		extractButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				boolean result;
				result = (!Functions.isEmptyOrNullString(initialFilePath.getText().trim())
						&& !Functions.isEmptyOrNullString(hadoopFilePath.getText().trim())
						&& !Functions.isEmptyOrNullString(outputDir.getText().trim())
						&& !Functions.isEmptyOrNullString(minSupThreshold.getText().trim())
						&& !Functions.isEmptyOrNullString(minConfThreshold.getText().trim()));
				if (result) {
					resObj.setInitialDataFile(initialFilePath.getText().trim());
					resObj.setHadoopResultsFile(hadoopFilePath.getText());
					resObj.setOutputFolder(outputDir.getText());
					resObj.setMinimumSupport(new Double(minSupThreshold.getText().trim()));
					resObj.setMinimumConfidence(new Double(minConfThreshold.getText().trim()));
					resObj.setInitialCount(Functions.readTXTFile(initialFilePath.getText()).split("\n").length);
					JOptionPane.showMessageDialog(guiFrame, "All parameteres have been set. Extracting rules...");
					ExtractionFunctions.itemSetFunction(resObj);
					ExtractionFunctions.ruleFunction(resObj);
					JOptionPane.showMessageDialog(guiFrame, "Rule extraction finished!");
					nextButton.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(guiFrame, "Not all parameteres have been set. Cannot proceed.");
				}
			}
		});
		
		JButton helpButton = new JButton("Help");
		helpButton.setBounds(482, 320, 90, 30);
		extractionPanel.add(helpButton);
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				/* to be implemented, change the help text */
				JOptionPane.showMessageDialog(guiFrame,
						"Instructions follow:" + "\nChoose a BasePath to enable other paths and proceeding"
								+ "\nOther paths are set at default values, but may be edited"
								+ "\nThe save button will save the current configuration to an xml file"
								+ "\nThe next button will lead to the execution screen");
			}
		});

		JButton initialFileChooser = new JButton("...");
		initialFileChooser.setBounds(541, 90, 31, 20);
		extractionPanel.add(initialFileChooser);
		initialFileChooser.setEnabled(true);
		initialFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					initialFilePath.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JButton hadoopFileChooser = new JButton("...");
		hadoopFileChooser.setBounds(541, 140, 31, 20);
		extractionPanel.add(hadoopFileChooser);
		hadoopFileChooser.setEnabled(true);
		hadoopFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					hadoopFilePath.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JButton outputDirChooser = new JButton("...");
		outputDirChooser.setBounds(541, 190, 31, 20);
		extractionPanel.add(outputDirChooser);
		outputDirChooser.setEnabled(true);
		outputDirChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel importPanel = new JPanel();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(importPanel) == 0) {
					outputDir.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});

		JLabel extractionLabel = new JLabel("Rule Extraction Screen");
		extractionLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		extractionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		extractionLabel.setBounds(207, 11, 238, 30);
		extractionPanel.add(extractionLabel);

		guiFrame.setVisible(true);
		guiFrame.revalidate();
	}

	protected static void rulesScreen(JFrame guiFrame, ResultsObject resObj) {

		guiFrame.setTitle("Association Rules Screen");
		JPanel rulesResultsPanel = new JPanel();
		guiFrame.getContentPane().add(rulesResultsPanel);
		rulesResultsPanel.setBounds(guiFrame.getBounds());
		rulesResultsPanel.setLayout(null);

		JButton backButton = new JButton("To Main Menu");
		backButton.setBounds(30, 402, 130, 30);
		rulesResultsPanel.add(backButton);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				guiFrame.remove(rulesResultsPanel);
				guiFrame.setVisible(false);
				mainMenuScreen(guiFrame);
			}
		});

		JButton saveButton = new JButton("Export to XML");
		saveButton.setBounds(465, 402, 130, 30);
		rulesResultsPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				/* handle creation and messages here */
				 dmarm.utilities.ReportGenerator.generateReportFile(resObj);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 31, 565, 360);
		rulesResultsPanel.add(scrollPane);

		JTable resultsTable = new JTable();
		scrollPane.setViewportView(resultsTable);
		resultsTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		

		
		
		/* it is moved inside rule function */
//		/* Filter result rules based on minimum support and minimum confidence */
//		 ExtractionFunctions.filterRules(resObj);
//
//		/* remove brackets from rules */
//		 ExtractionFunctions.removeBrackets(resObj);
		/* it is moved inside rule function */
		
		
		
		
		// fill data object from rules array
		 Object [][] data = new Object[resObj.getRules().length][5];
		 for (int i = 0; i<resObj.getRules().length;i++) { 
		//generation id's are not accurate
		 data[i][0] = i+1;
		 data[i][1] = resObj.getRules()[i].getAntecedent();
		 data[i][2] = resObj.getRules()[i].getConsequent();
		 data[i][3] = resObj.getRules()[i].getSupport();
		 data[i][4] = resObj.getRules()[i].getConfidence();
		 }

		String[] columnNames = new String[] { "No", "Andecedent", "Consequent", "Support", "Confidence" };

		resultsTable.setModel(new DefaultTableModel(data, columnNames) {
			Class[] columnTypes = new Class[] { Integer.class, String.class, String.class, Double.class, Double.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});

		resultsTable.getColumnModel().getColumn(0).setResizable(false);
		resultsTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		resultsTable.getColumnModel().getColumn(0).setMinWidth(25);
		resultsTable.getColumnModel().getColumn(0).setMaxWidth(25);
		resultsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		resultsTable.getColumnModel().getColumn(1).setMinWidth(40);
		resultsTable.getColumnModel().getColumn(1).setMaxWidth(400);
		resultsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		resultsTable.getColumnModel().getColumn(2).setMinWidth(40);
		resultsTable.getColumnModel().getColumn(2).setMaxWidth(400);
		resultsTable.getColumnModel().getColumn(3).setResizable(false);
		resultsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		resultsTable.getColumnModel().getColumn(3).setMinWidth(50);
		resultsTable.getColumnModel().getColumn(3).setMaxWidth(50);
		resultsTable.getColumnModel().getColumn(4).setResizable(false);
		resultsTable.getColumnModel().getColumn(4).setPreferredWidth(65);
		resultsTable.getColumnModel().getColumn(4).setMinWidth(65);
		resultsTable.getColumnModel().getColumn(4).setMaxWidth(65);
		resultsTable.setFillsViewportHeight(true);
		resultsTable.setColumnSelectionAllowed(true);
		resultsTable.setCellSelectionEnabled(true);

		JLabel title = new JLabel("Extracted Rules");
		title.setFont(new Font("Tahoma", Font.PLAIN, 18));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(30, 0, 576, 32);
		rulesResultsPanel.add(title);

		guiFrame.setVisible(true);
		guiFrame.revalidate();
	}
}
