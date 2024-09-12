package gui;

import javax.swing.JFrame;

/**
 * 
 * @description Distributed Multi-Level Association Rule Mining (DMARM)
 *
 */
public class DmarmApplication {

	public static void main(String[] args) {
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setSize(640, 480);
		guiFrame.setResizable(false);
		GUIScreens.mainMenuScreen(guiFrame);
	}

}
