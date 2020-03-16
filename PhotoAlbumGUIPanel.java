import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Displays a JList of Photos and a preview of the photo. This class manages layout of controls and
 * also handles events.
 * 
 * 1. The index is updated each time a button is clicked. 2. We will use the same concept to play
 * songs in the playlist.
 *
 * @author CS121 Instructors
 * @author Matt Fuller
 */
@SuppressWarnings("serial")
public class PhotoAlbumGUIPanel extends JPanel {
	/** The data representing the list of photos in the album (the "model") */
	private PhotoAlbum album;

	/**
	 * The GUI representation of the list of photos in the album (the "view")
	 */
	private JList<Photo> photoList;

	private JLabel imageLabel; // Displays the image icon inside of the preview
	                           // panel
	private JButton nextButton; // Selects the next image in the photoList
	private JButton prevButton; // Selects the previous image in the photoList
	private int index = 0;

	/**
	 * Instantiates the photo album and adds all of the components to the JPanel.
	 */
	public PhotoAlbumGUIPanel()
		{
			// The entire frame will have a BorderLayout (we will be adding more to
			// it
			// in the next lab).
			setLayout(new BorderLayout());

			// All of your work in this lab will go in the left panel.
			// The right panel will be added in the next lab.
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			add(leftPanel);

			// Instantiate the album object and print it to the command line
			// to make sure it worked.
			album = new PhotoAlbum("Boise", "photos/album.dat");
			System.out.println(album);

			// Instantiates the JList<Photo> photoList object (declared above) and
			// set the list data to album.getPhotoArray().
			// Set the selected index of the photoList to position 0 to select the
			// first photo by default.
			photoList = new JList<Photo>(album.getPhotoArray());
			photoList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			photoList.setSelectedIndex(0);

			// Creates a JScrollPane containing the photoList.
			JScrollPane scrollPane = new JScrollPane(photoList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			// Adds the scrollPane to the leftPanel.
			leftPanel.add(scrollPane);

			// Creates a new preview sub-panel.
			JPanel previewPanel = new JPanel();
			imageLabel = new JLabel();

			// Adds the imageLabel to the previewPanel and add the previewPanel to
			// the leftPanel
			previewPanel.add(imageLabel);
			leftPanel.add(previewPanel);

			// Adds a ListSelectionListener to the photoList.
			photoList.setSelectedIndex(index);
			displayPhoto(photoList.getSelectedValue());

			// Creates a sub-panel for control buttons.
			// Initializes the prevButton and nextButton and adds the
			// sameControlListener to both.
			JPanel controlPanel = new JPanel();
			prevButton = new JButton(">");
			nextButton = new JButton("<");

			ControlListener listener = new ControlListener();
			nextButton.addActionListener(listener);
			prevButton.addActionListener(listener);

			PhotoListListener photoListener = new PhotoListListener();
			photoList.addListSelectionListener(photoListener);

			// Adds both buttons to the controlPanel and adds the controlPanel
			// to the leftPanel.
			controlPanel.add(nextButton);
			controlPanel.add(prevButton);
			leftPanel.add(controlPanel);
		}

	private class PhotoListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// Uses the displayPhoto() method (provided below) to display the
			// photo currently selected in the photoList.
			displayPhoto(photoList.getSelectedValue());
		}
	}

	private class ControlListener implements ActionListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event. ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			// Find index of photo that is currently selected.
			int index = photoList.getSelectedIndex();

			// Update the index depending on which button was clicked.
			if (event.getSource() == nextButton) {
				index = index - 1;
				if (index < 0) {
					index = album.getNumPhotos() - 1;
				}
			} else {
				index = index + 1;
				if (index >= album.getNumPhotos()) {
					index = 0;
				}
			}
			photoList.setSelectedIndex(index);

			// Displays the currently selected photo in the photList
			displayPhoto(photoList.getSelectedValue());
		}
	}

	/**
	 * Updates the photo on the preview panel.
	 * 
	 * @param photo The photo to display.
	 */
	private void displayPhoto(Photo photo) {
		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(photo.getFile()));
			imageLabel.setIcon(icon);

		}
		catch (IOException ex) {
			imageLabel.setText("Image not found :(");
		}
	}

}
