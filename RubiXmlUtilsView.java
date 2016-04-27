package com.jpmorgan.rubi.xml.utils.view;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.jpmorgan.rubi.xml.utils.controller.RubiXmlCoreController;
import com.jpmorgan.rubi.xml.utils.dto.RubiXmlControllerDTO;
import com.jpmorgan.rubi.xml.utils.dto.RubiXmlDeleteNodeDTO;
import com.jpmorgan.rubi.xml.utils.dto.RubiXmlInsertNodeDTO;
import com.jpmorgan.rubi.xml.utils.dto.RubiXmlReplaceNodeDTO;
import com.jpmorgan.rubi.xml.utils.dto.RubiXmlReplaceNodeValueDTO;
import com.jpmorgan.rubi.xml.utils.util.RubiLimitClientConstants;
import com.jpmorgan.rubi.xml.utils.util.RubiLimitCommonComponent;

public class RubiXmlUtilsView extends ViewPart {
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	public static final String ID = "rubi-xml-utils.view";

	// private FormToolkit toolkit;

	// private static Shell shell;

	private Browser browser;

	// private int test = 1;
	// ********** All Composite ***********//
	private Composite body;
	private Composite commonComposite;
	// ********** All Composite ***********//

	// ******** All Group Variables *********//
	private Group insertNewNodeGroup;
	private Group replaceNewNodeGroup;
	private Group replaceNewNodeValueGroup;
	private Group deleteNodeGroup;
	// ******** All Group Variables ********//

	// ******** All Button Variables *******//
	private Button insertButton;
	private Button replaceTagButton;
	private Button replaceTagValueButton;
	private Button deleteTagButton;

	// ******** All Button Variables ********//

	// ******** All Text Boxes Variables ********//
	private Text inputDirText;
	private Text nodeNameText;
	private Text newNodeText;
	private Text attributeNameText;
	private Text attributeValueText;
	private Text nodeValueText;
	private Text insertBeforeTagText;
	private Text xpathText;
	private Text totalNumberOfXmlText;
	private Text noOfUpdatedFileText;
	private Text nodeNotFoundNoText;
	private Text noOfScemaValidationFailedText;
	// ******** All Text Boxes Variables ********//
	private Font boldFont;
	private Image image;
	private Shell shell;

	private Label totalNumberOfXml;
	private Label noOfUpdatedFile;
	private Label nodeNotFoundNo;
	private Label noOfScemaValidationFailed;

	RubiXmlCoreController rubiXmlCoreController = new RubiXmlCoreController();
	RubiXmlInsertNodeDTO rubiXmlInsertNodeDTO = new RubiXmlInsertNodeDTO();
	RubiXmlReplaceNodeDTO rubiXmlReplaceNodeDTO = new RubiXmlReplaceNodeDTO();
	RubiXmlReplaceNodeValueDTO rubiXmlReplaceNodeValueDTO = new RubiXmlReplaceNodeValueDTO();
	RubiXmlDeleteNodeDTO rubiXmlDeleteNodeDTO = new RubiXmlDeleteNodeDTO();

	@Override
	public void createPartControl(final Composite parent) {

		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		parent.getShell().setSize(1000, 600);
		final CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 0, 0));
		tabFolder.setSimple(false);
		tabFolder.setUnselectedImageVisible(false);
		tabFolder.setUnselectedCloseVisible(false);

		final FontData[] boldFontData = getModifiedFontData(Display.getCurrent().getSystemFont().getFontData(), SWT.BOLD);
		boldFont = new Font(Display.getCurrent(), boldFontData);
		// final ScrolledForm form = toolkit.createScrolledForm(shell);
		// body = form.getBody();
		Display display = Display.getCurrent();
		int colorCount = 3;
		Color[] colors = new Color[colorCount];
		colors[0] = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		colors[1] = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		colors[2] = colors[0];
		int[] percents = new int[colorCount - 1];
		percents[0] = 4;
		percents[1] = 60;
		tabFolder.setSelectionBackground(colors, percents, true);
		tabFolder.setSelectionForeground(display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND));

		CTabItem insertTagTab = new CTabItem(tabFolder, SWT.FILL);
		insertTagTab.setText("INSERT NEW TAG");

		Composite body = new Composite(tabFolder, SWT.NONE);
		body.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		final Group insertTagGroup = createComponentsInsertGroup(tabFolder, body, toolkit);
		insertTagTab.setControl(insertTagGroup);

		CTabItem replaceTagTab = new CTabItem(tabFolder, SWT.NONE);
		replaceTagTab.setText("REPLACE TAG");
		final Group replaceTagGroup = createComponentsReplaceNodeGroup(tabFolder, body, toolkit);
		replaceTagTab.setControl(replaceTagGroup);

		CTabItem replaceTagValue = new CTabItem(tabFolder, SWT.NONE);
		replaceTagValue.setText("REPLACE TAG VALUE");
		final Group replaceTagValueGroup = createComponentsNodeValueGroup(tabFolder, body, toolkit);
		replaceTagValue.setControl(replaceTagValueGroup);

		CTabItem deleteTag = new CTabItem(tabFolder, SWT.NONE);
		deleteTag.setText("DELETE TAG");
		final Group deleteTagGroup = createComponentsDeleteGroup(tabFolder, body, toolkit);
		deleteTag.setControl(deleteTagGroup);

		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetScreenValues();
				resetCommonComponent();
				resetController();
			}
		});

		insertButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent slectionEvent) {
				try {
					resetController();
					String inputDirValue = rubiXmlInsertNodeDTO.getInputDirText().getText();
					String nodeNameValue = rubiXmlInsertNodeDTO.getRootNodeText().getText();
					String newNodeValue = rubiXmlInsertNodeDTO.getNewNodeNameText().getText();
					String textValue = rubiXmlInsertNodeDTO.getNodeValueText().getText();
					String attNameValue = rubiXmlInsertNodeDTO.getAttributeNameText().getText();
					String attValue = rubiXmlInsertNodeDTO.getAttributeValueText().getText();
					String beforeNodeValue = rubiXmlInsertNodeDTO.getInsertBeforeNodeText().getText();
					String xpathValue = rubiXmlInsertNodeDTO.getXpathText().getText();

					RubiXmlControllerDTO rubiXmlControllerDTO = new RubiXmlControllerDTO();
					rubiXmlControllerDTO.setInputDirectory(inputDirValue);
					rubiXmlControllerDTO.setTagName(nodeNameValue);
					rubiXmlControllerDTO.setNewTagName(newNodeValue);
					rubiXmlControllerDTO.setAttributeName(attNameValue);
					rubiXmlControllerDTO.setAttributeValue(attValue);
					rubiXmlControllerDTO.setTagTextValue(textValue);
					rubiXmlControllerDTO.setInsertBeforeTag(beforeNodeValue);
					rubiXmlControllerDTO.setXpathOfTag(xpathValue);
					rubiXmlControllerDTO.setSubmitButton(rubiXmlInsertNodeDTO.getInsertButton().getText());

					if (inputDirValue.isEmpty() || inputDirValue == null || (nodeNameValue.isEmpty() || nodeNameValue == null)
							&& (xpathValue == null || xpathValue.isEmpty()) || newNodeValue.isEmpty() || newNodeValue == null || textValue.isEmpty()
							|| textValue == null) {
						MessageDialog.openError(shell, "Error", "Mandotory fields cannot be empty");
						return;
					}
					if (!nodeNameValue.isEmpty() && nodeNameValue != null && xpathValue != null && !xpathValue.isEmpty()) {
						MessageDialog.openError(shell, "Error", "Please select one Root Tag name");
						return;
					}
					rubiXmlCoreController.xmlController(rubiXmlControllerDTO);

					rubiXmlInsertNodeDTO.getTotalNumberOfXmlText().setText(rubiXmlCoreController.getTotalFileCounter() + "");
					rubiXmlInsertNodeDTO.getNoOfScemaValidationFailedText().setText(rubiXmlCoreController.getInfectedFileSize() + "");
					rubiXmlInsertNodeDTO.getNodeNotFoundNoText().setText(rubiXmlCoreController.getNodeNotFoundFileCounter() + "");
					rubiXmlInsertNodeDTO.getNoOfUpdatedFileText().setText(rubiXmlCoreController.getUpdatedFileCounter() + "");

					rubiXmlInsertNodeDTO.getTotalNumberOfXml().setVisible(true);
					rubiXmlInsertNodeDTO.getTotalNumberOfXmlText().setVisible(true);
					rubiXmlInsertNodeDTO.getNoOfScemaValidationFailedText().setVisible(true);
					rubiXmlInsertNodeDTO.getNoOfScemaValidationFailed().setVisible(true);
					rubiXmlInsertNodeDTO.getNodeNotFoundNo().setVisible(true);
					rubiXmlInsertNodeDTO.getNodeNotFoundNoText().setVisible(true);
					rubiXmlInsertNodeDTO.getNoOfUpdatedFile().setVisible(true);
					rubiXmlInsertNodeDTO.getNoOfUpdatedFileText().setVisible(true);

				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		replaceTagButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent slectionEvent) {
				try {

					String inputDirValue = rubiXmlReplaceNodeDTO.getInputDirText().getText();
					String nodeNameValue = rubiXmlReplaceNodeDTO.getOldNodeText().getText();
					String newNodeValue = rubiXmlReplaceNodeDTO.getNewNodeText().getText();
					String xpathValue = rubiXmlReplaceNodeDTO.getXpathText().getText();

					RubiXmlControllerDTO rubiXmlControllerDTO = new RubiXmlControllerDTO();
					rubiXmlControllerDTO.setInputDirectory(inputDirValue);
					rubiXmlControllerDTO.setTagName(nodeNameValue);
					rubiXmlControllerDTO.setNewTagName(newNodeValue);
					rubiXmlControllerDTO.setXpathOfTag(xpathValue);
					rubiXmlControllerDTO.setSubmitButton(rubiXmlReplaceNodeDTO.getReplaceNodeButton().getText());

					if (inputDirValue.isEmpty() || inputDirValue == null || (nodeNameValue.isEmpty() || nodeNameValue == null)
							&& (xpathValue == null || xpathValue.isEmpty()) || newNodeValue.isEmpty() || newNodeValue == null) {
						MessageDialog.openError(shell, "Error", "Mandotory fields cannot be empty");
						return;
					}
					if (!nodeNameValue.isEmpty() && nodeNameValue != null && xpathValue != null && !xpathValue.isEmpty()) {
						MessageDialog.openError(shell, "Error", "Please select one Root Tag name");
						return;
					}
					rubiXmlCoreController.xmlController(rubiXmlControllerDTO);
					// ////////
					rubiXmlReplaceNodeDTO.getTotalNumberOfXmlText().setText(rubiXmlCoreController.getTotalFileCounter() + "");
					rubiXmlReplaceNodeDTO.getNoOfScemaValidationFailedText().setText(rubiXmlCoreController.getInfectedFileSize() + "");
					rubiXmlReplaceNodeDTO.getNodeNotFoundNoText().setText(rubiXmlCoreController.getNodeNotFoundFileCounter() + "");
					rubiXmlReplaceNodeDTO.getNoOfUpdatedFileText().setText(rubiXmlCoreController.getUpdatedFileCounter() + "");

					rubiXmlReplaceNodeDTO.getTotalNumberOfXml().setVisible(true);
					rubiXmlReplaceNodeDTO.getTotalNumberOfXmlText().setVisible(true);
					rubiXmlReplaceNodeDTO.getNoOfScemaValidationFailedText().setVisible(true);
					rubiXmlReplaceNodeDTO.getNoOfScemaValidationFailed().setVisible(true);
					rubiXmlReplaceNodeDTO.getNodeNotFoundNo().setVisible(true);
					rubiXmlReplaceNodeDTO.getNodeNotFoundNoText().setVisible(true);
					rubiXmlReplaceNodeDTO.getNoOfUpdatedFile().setVisible(true);
					rubiXmlReplaceNodeDTO.getNoOfUpdatedFileText().setVisible(true);
					// ////////

				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		replaceTagValueButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent slectionEvent) {
				try {

					String inputDirValue = rubiXmlReplaceNodeValueDTO.getInputDirText().getText();
					String nodeNameValue = rubiXmlReplaceNodeValueDTO.getNodeNameText().getText();
					String newNodeValue = rubiXmlReplaceNodeValueDTO.getNodeTextValueText().getText();
					String xpathValue = rubiXmlReplaceNodeValueDTO.getXpathText().getText();
					RubiXmlControllerDTO rubiXmlControllerDTO = new RubiXmlControllerDTO();
					rubiXmlControllerDTO.setInputDirectory(inputDirValue);
					rubiXmlControllerDTO.setTagName(nodeNameValue);
					rubiXmlControllerDTO.setTagTextValue(newNodeValue);

					rubiXmlControllerDTO.setSubmitButton(rubiXmlReplaceNodeValueDTO.getUpdateNodeValueButton().getText());
					if (inputDirValue.isEmpty() || inputDirValue == null || (nodeNameValue.isEmpty() || nodeNameValue == null)
							&& (xpathValue == null || xpathValue.isEmpty()) || newNodeValue.isEmpty() || newNodeValue == null) {
						MessageDialog.openError(shell, "Error", "Mandotory fields cannot be empty");
						return;
					}

					if (!nodeNameValue.isEmpty() && nodeNameValue != null && xpathValue != null && !xpathValue.isEmpty()) {
						MessageDialog.openError(shell, "Error", "Please select one Root Tag name");
						return;
					}
					rubiXmlCoreController.xmlController(rubiXmlControllerDTO);

					rubiXmlReplaceNodeValueDTO.getTotalNumberOfXmlText().setText(rubiXmlCoreController.getTotalFileCounter() + "");
					rubiXmlReplaceNodeValueDTO.getNoOfScemaValidationFailedText().setText(rubiXmlCoreController.getInfectedFileSize() + "");
					rubiXmlReplaceNodeValueDTO.getNodeNotFoundNoText().setText(rubiXmlCoreController.getNodeNotFoundFileCounter() + "");
					rubiXmlReplaceNodeValueDTO.getNoOfUpdatedFileText().setText(rubiXmlCoreController.getUpdatedFileCounter() + "");

					rubiXmlReplaceNodeValueDTO.getTotalNumberOfXml().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getTotalNumberOfXmlText().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getNoOfScemaValidationFailedText().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getNoOfScemaValidationFailed().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getNodeNotFoundNo().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getNodeNotFoundNoText().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getNoOfUpdatedFile().setVisible(true);
					rubiXmlReplaceNodeValueDTO.getNoOfUpdatedFileText().setVisible(true);
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		deleteTagButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent slectionEvent) {
				try {
					String inputDirValue = rubiXmlDeleteNodeDTO.getInputDirText().getText();
					String nodeNameValue = rubiXmlDeleteNodeDTO.getNodeNameText().getText();
					String xpathValue = rubiXmlDeleteNodeDTO.getXpathText().getText();

					RubiXmlControllerDTO rubiXmlControllerDTO = new RubiXmlControllerDTO();
					rubiXmlControllerDTO.setInputDirectory(inputDirValue);
					rubiXmlControllerDTO.setTagName(nodeNameValue);
					rubiXmlControllerDTO.setXpathOfTag(xpathValue);
					rubiXmlControllerDTO.setSubmitButton(rubiXmlDeleteNodeDTO.getDeleteButton().getText());

					if (inputDirValue.isEmpty() || inputDirValue == null || (nodeNameValue.isEmpty() || nodeNameValue == null)
							&& (xpathValue == null || xpathValue.isEmpty())) {
						MessageDialog.openError(shell, "Error", "Mandotory fields cannot be empty");
						return;
					}

					if (!nodeNameValue.isEmpty() && nodeNameValue != null && xpathValue != null && !xpathValue.isEmpty()) {
						MessageDialog.openError(shell, "Error", "Please select one Root Tag name");
						return;
					}
					rubiXmlCoreController.xmlController(rubiXmlControllerDTO);

					rubiXmlDeleteNodeDTO.getTotalNumberOfXmlText().setText(rubiXmlCoreController.getTotalFileCounter() + "");
					rubiXmlDeleteNodeDTO.getNoOfScemaValidationFailedText().setText(rubiXmlCoreController.getInfectedFileSize() + "");
					rubiXmlDeleteNodeDTO.getNodeNotFoundNoText().setText(rubiXmlCoreController.getNodeNotFoundFileCounter() + "");
					rubiXmlDeleteNodeDTO.getNoOfUpdatedFileText().setText(rubiXmlCoreController.getUpdatedFileCounter() + "");

					rubiXmlDeleteNodeDTO.getTotalNumberOfXml().setVisible(true);
					rubiXmlDeleteNodeDTO.getTotalNumberOfXmlText().setVisible(true);
					rubiXmlDeleteNodeDTO.getNoOfScemaValidationFailedText().setVisible(true);
					rubiXmlDeleteNodeDTO.getNoOfScemaValidationFailed().setVisible(true);
					rubiXmlDeleteNodeDTO.getNodeNotFoundNo().setVisible(true);
					rubiXmlDeleteNodeDTO.getNodeNotFoundNoText().setVisible(true);
					rubiXmlDeleteNodeDTO.getNoOfUpdatedFile().setVisible(true);
					rubiXmlDeleteNodeDTO.getNoOfUpdatedFileText().setVisible(true);
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		if (body != null) {
			body.setFocus();
		}
	}

	public Group createComponentsInsertGroup(CTabFolder tabFolder, Composite body, final FormToolkit toolkit) {

		insertNewNodeGroup = RubiLimitCommonComponent.createGroup(6, "", 400, 220, true, tabFolder, SWT.NONE, toolkit);

		final Composite insertNodeComposite = new Composite(insertNewNodeGroup, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout(1, false);
		insertNodeComposite.setLayout(compositeLayout);
		final GridData insertNodeCompositeGD = new GridData(1200, 69, true, false, 6, 0);
		insertNodeComposite.setLayoutData(insertNodeCompositeGD);
		insertNodeComposite.setBackground(body.getBackground());

		insertNodeComposite.setVisible(true);

		browser = new Browser(insertNodeComposite, SWT.BORDER); // Uses IE on MS Windows
		browser.setSize(1360, 67);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Input Directory*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		inputDirText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 400, SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		inputDirText.setText("D:/test/");
		rubiXmlInsertNodeDTO.setInputDirText(inputDirText);

		final Button browseButton =
				RubiLimitCommonComponent.createButton(null, insertNewNodeGroup, "Browse", 80, SWT.LEFT, SWT.CENTER, true, false, 4, 0, SWT.NONE,
						toolkit);
		browseButton.setVisible(false);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Root Node Name*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		nodeNameText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		rubiXmlInsertNodeDTO.setRootNodeText(nodeNameText);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Node Name*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		newNodeText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 3, 0);
		newNodeText.setText("dvsdf");
		rubiXmlInsertNodeDTO.setNewNodeNameText(newNodeText);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Node Text Value*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		nodeValueText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		nodeValueText.setText("sdsd");
		rubiXmlInsertNodeDTO.setNodeValueText(nodeValueText);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Attribute Name", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		attributeNameText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 3, 0);
		rubiXmlInsertNodeDTO.setAttributeNameText(attributeNameText);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Attribute Value", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		attributeValueText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		rubiXmlInsertNodeDTO.setAttributeValueText(attributeValueText);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Insert Before Node", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		insertBeforeTagText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 3, 0);
		rubiXmlInsertNodeDTO.setInsertBeforeNodeText(insertBeforeTagText);

		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Xpath of Tag", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		xpathText = RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		xpathText.setText("//header/messageId");

		rubiXmlInsertNodeDTO.setXpathText(xpathText);

		insertButton =
				RubiLimitCommonComponent.createButton(null, insertNewNodeGroup, "Insert Tag", 100, SWT.CENTER, SWT.NONE, true, false, 6, 0, SWT.NONE,
						toolkit);
		rubiXmlInsertNodeDTO.setInsertButton(insertButton);
		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);

		totalNumberOfXml =
				RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Total No. Of Xml File:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		totalNumberOfXml.setFont(boldFont);
		rubiXmlInsertNodeDTO.setTotalNumberOfXml(totalNumberOfXml);

		totalNumberOfXmlText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlInsertNodeDTO.setTotalNumberOfXmlText(totalNumberOfXmlText);

		noOfUpdatedFile =
				RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Updated File count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0, body);
		noOfUpdatedFile.setFont(boldFont);
		rubiXmlInsertNodeDTO.setNoOfUpdatedFile(noOfUpdatedFile);

		noOfUpdatedFileText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlInsertNodeDTO.setNoOfUpdatedFileText(noOfUpdatedFileText);

		nodeNotFoundNo =
				RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 140, "Node not found file count", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		nodeNotFoundNo.setFont(boldFont);
		rubiXmlInsertNodeDTO.setNodeNotFoundNo(nodeNotFoundNo);
		nodeNotFoundNoText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, false, false, 5, 0);
		rubiXmlInsertNodeDTO.setNodeNotFoundNoText(nodeNotFoundNoText);

		noOfScemaValidationFailed =
				RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 140, "Schema failed file count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		noOfScemaValidationFailed.setFont(boldFont);
		rubiXmlInsertNodeDTO.setNoOfScemaValidationFailed(noOfScemaValidationFailed);
		noOfScemaValidationFailedText =
				RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlInsertNodeDTO.setNoOfScemaValidationFailedText(noOfScemaValidationFailedText);
		return insertNewNodeGroup;

	}

	public Group createComponentsReplaceNodeGroup(CTabFolder tabFolder, Composite body, FormToolkit toolkit) {

		replaceNewNodeGroup = RubiLimitCommonComponent.createGroup(6, "", 400, 220, true, tabFolder, SWT.None, toolkit);

		final Composite insertNodeComposite = new Composite(replaceNewNodeGroup, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout(1, false);
		insertNodeComposite.setLayout(compositeLayout);
		final GridData insertNodeCompositeGD = new GridData(1200, 69, true, false, 6, 0);
		insertNodeComposite.setLayoutData(insertNodeCompositeGD);
		insertNodeComposite.setBackground(body.getBackground());

		insertNodeComposite.setVisible(true);

		browser = new Browser(insertNodeComposite, SWT.BORDER); // Uses IE on MS Windows
		browser.setSize(1360, 67);

		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 100, "Input Directory*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		inputDirText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.BORDER, 400, SWT.CENTER, SWT.CENTER, false, false, 0, 0);
		rubiXmlReplaceNodeDTO.setInputDirText(inputDirText);

		final Button browseButton =
				RubiLimitCommonComponent.createButton(null, replaceNewNodeGroup, "Browse", 80, SWT.LEFT, SWT.CENTER, true, false, 4, 0, SWT.NONE,
						toolkit);
		browseButton.setVisible(false);

		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 100, "Old Node*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		nodeNameText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		rubiXmlReplaceNodeDTO.setOldNodeText(nodeNameText);

		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "New Node Name*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		newNodeText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 3, 0);
		rubiXmlReplaceNodeDTO.setNewNodeText(newNodeText);

		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 100, "Xpath of Tag", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		xpathText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeDTO.setXpathText(xpathText);
		replaceTagButton =
				RubiLimitCommonComponent.createButton(null, replaceNewNodeGroup, "Replace Tag", 100, SWT.CENTER, SWT.NONE, true, false, 6, 0,
						SWT.NONE, toolkit);
		rubiXmlReplaceNodeDTO.setReplaceNodeButton(replaceTagButton);

		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		totalNumberOfXml =
				RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "Total No. Of Xml File:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		totalNumberOfXml.setFont(boldFont);
		rubiXmlReplaceNodeDTO.setTotalNumberOfXml(totalNumberOfXml);

		totalNumberOfXmlText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeDTO.setTotalNumberOfXmlText(totalNumberOfXmlText);

		noOfUpdatedFile =
				RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 120, "Updated File count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0, body);
		noOfUpdatedFile.setFont(boldFont);
		rubiXmlReplaceNodeDTO.setNoOfUpdatedFile(noOfUpdatedFile);

		noOfUpdatedFileText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeDTO.setNoOfUpdatedFileText(noOfUpdatedFileText);

		nodeNotFoundNo =
				RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 140, "Node not found file count", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		nodeNotFoundNo.setFont(boldFont);
		rubiXmlReplaceNodeDTO.setNodeNotFoundNo(nodeNotFoundNo);
		nodeNotFoundNoText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, false, false, 5, 0);
		rubiXmlReplaceNodeDTO.setNodeNotFoundNoText(nodeNotFoundNoText);

		noOfScemaValidationFailed =
				RubiLimitCommonComponent.createLabel(replaceNewNodeGroup, 140, "Schema failed file count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		noOfScemaValidationFailed.setFont(boldFont);
		rubiXmlReplaceNodeDTO.setNoOfScemaValidationFailed(noOfScemaValidationFailed);
		noOfScemaValidationFailedText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeDTO.setNoOfScemaValidationFailedText(noOfScemaValidationFailedText);
		return replaceNewNodeGroup;
	}

	public Group createComponentsNodeValueGroup(CTabFolder tabFolder, Composite body, FormToolkit toolkit) {
		replaceNewNodeValueGroup = RubiLimitCommonComponent.createGroup(6, "", 400, 220, true, tabFolder, SWT.None, toolkit);

		final Composite insertNodeComposite = new Composite(replaceNewNodeValueGroup, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout(1, false);
		insertNodeComposite.setLayout(compositeLayout);
		final GridData insertNodeCompositeGD = new GridData(1200, 69, true, false, 6, 0);
		insertNodeComposite.setLayoutData(insertNodeCompositeGD);
		insertNodeComposite.setBackground(body.getBackground());

		insertNodeComposite.setVisible(true);

		browser = new Browser(insertNodeComposite, SWT.BORDER); // Uses IE on MS Windows
		browser.setSize(1360, 67);

		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "Input Directory*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		inputDirText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.BORDER, 400, SWT.CENTER, SWT.CENTER, false, false,
						0, 0);
		rubiXmlReplaceNodeValueDTO.setInputDirText(inputDirText);

		final Button browseButton =
				RubiLimitCommonComponent.createButton(null, replaceNewNodeValueGroup, "Browse", 80, SWT.LEFT, SWT.CENTER, true, false, 4, 0,
						SWT.NONE, toolkit);
		browseButton.setVisible(false);

		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "Node Name*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		nodeNameText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, false, false, 0,
						0);
		rubiXmlReplaceNodeValueDTO.setNodeNameText(nodeNameText);

		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "Node Text Value*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		nodeValueText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 3,
						0);
		rubiXmlReplaceNodeValueDTO.setNodeTextValueText(nodeValueText);
		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "Xpath of Tag", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		xpathText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 5,
						0);
		rubiXmlReplaceNodeValueDTO.setXpathText(xpathText);

		replaceTagValueButton =
				RubiLimitCommonComponent.createButton(null, replaceNewNodeValueGroup, "Replace Tag Value", 100, SWT.CENTER, SWT.NONE, true, false, 6,
						0, SWT.NONE, toolkit);
		rubiXmlReplaceNodeValueDTO.setUpdateNodeValueButton(replaceTagValueButton);

		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		totalNumberOfXml =
				RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "Total No. Of Xml File:", SWT.LEFT, SWT.CENTER, false, false, 0,
						0, body);
		rubiXmlReplaceNodeValueDTO.setTotalNumberOfXml(totalNumberOfXml);

		totalNumberOfXmlText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeValueDTO.setTotalNumberOfXmlText(totalNumberOfXmlText);

		noOfUpdatedFile =
				RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 120, "Updated File count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		rubiXmlReplaceNodeValueDTO.setNoOfUpdatedFile(noOfUpdatedFile);

		noOfUpdatedFileText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeValueDTO.setNoOfUpdatedFileText(noOfUpdatedFileText);

		nodeNotFoundNo =
				RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 140, "Node not found file count", SWT.LEFT, SWT.CENTER, false, false,
						0, 0, body);
		rubiXmlReplaceNodeValueDTO.setNodeNotFoundNo(nodeNotFoundNo);
		nodeNotFoundNoText =
				RubiLimitCommonComponent
						.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, false, false, 5, 0);
		rubiXmlReplaceNodeValueDTO.setNodeNotFoundNoText(nodeNotFoundNoText);

		noOfScemaValidationFailed =
				RubiLimitCommonComponent.createLabel(replaceNewNodeValueGroup, 140, "Schema failed file count:", SWT.LEFT, SWT.CENTER, false, false,
						0, 0, body);
		rubiXmlReplaceNodeValueDTO.setNoOfScemaValidationFailed(noOfScemaValidationFailed);

		noOfScemaValidationFailedText =
				RubiLimitCommonComponent.createTextBox(toolkit, replaceNewNodeValueGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlReplaceNodeValueDTO.setNoOfScemaValidationFailedText(noOfScemaValidationFailedText);

		totalNumberOfXml.setFont(boldFont);
		noOfUpdatedFile.setFont(boldFont);
		nodeNotFoundNo.setFont(boldFont);
		noOfScemaValidationFailed.setFont(boldFont);
		return replaceNewNodeValueGroup;
	}

	public Group createComponentsDeleteGroup(CTabFolder tabFolder, Composite body, FormToolkit toolkit) {
		deleteNodeGroup = RubiLimitCommonComponent.createGroup(6, "", 400, 220, true, tabFolder, SWT.None, toolkit);

		final Composite insertNodeComposite = new Composite(deleteNodeGroup, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout(1, false);
		insertNodeComposite.setLayout(compositeLayout);
		final GridData insertNodeCompositeGD = new GridData(1200, 69, true, false, 6, 0);
		insertNodeComposite.setLayoutData(insertNodeCompositeGD);
		insertNodeComposite.setBackground(body.getBackground());

		insertNodeComposite.setVisible(true);

		browser = new Browser(insertNodeComposite, SWT.BORDER); // Uses IE on MS Windows
		browser.setSize(1360, 67);

		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 100, "Input Directory*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		inputDirText =
				RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.BORDER, 400, SWT.CENTER, SWT.CENTER, false, false, 0, 0);
		rubiXmlDeleteNodeDTO.setInputDirText(inputDirText);

		final Button browseButton =
				RubiLimitCommonComponent
						.createButton(null, deleteNodeGroup, "Browse", 80, SWT.LEFT, SWT.CENTER, true, false, 4, 0, SWT.NONE, toolkit);
		browseButton.setVisible(false);

		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 100, "Node Name*", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);
		nodeNameText =
				RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		rubiXmlDeleteNodeDTO.setNodeNameText(nodeNameText);

		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 100, "Xpath of Tag", SWT.CENTER, SWT.CENTER, false, false, 0, 0, body);

		xpathText = RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.BORDER, 250, SWT.LEFT, SWT.CENTER, true, false, 3, 0);
		rubiXmlDeleteNodeDTO.setXpathText(xpathText);

		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 100, "", SWT.LEFT, SWT.CENTER, true, false, 6, 0, body);

		deleteTagButton =
				RubiLimitCommonComponent.createButton(null, deleteNodeGroup, "Delete Tag", 100, SWT.CENTER, SWT.NONE, true, false, 6, 0, SWT.NONE,
						toolkit);
		rubiXmlDeleteNodeDTO.setDeleteButton(deleteTagButton);

		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);
		RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "", SWT.CENTER, SWT.CENTER, true, false, 6, 0, body);

		totalNumberOfXml =
				RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "Total No. Of Xml File:", SWT.LEFT, SWT.CENTER, false, false, 0, 0, body);
		rubiXmlDeleteNodeDTO.setTotalNumberOfXml(totalNumberOfXml);

		totalNumberOfXmlText =
				RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlDeleteNodeDTO.setTotalNumberOfXmlText(totalNumberOfXmlText);

		noOfUpdatedFile =
				RubiLimitCommonComponent.createLabel(deleteNodeGroup, 120, "Updated File count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0, body);
		rubiXmlDeleteNodeDTO.setNoOfUpdatedFile(noOfUpdatedFile);

		noOfUpdatedFileText =
				RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlDeleteNodeDTO.setNoOfUpdatedFileText(noOfUpdatedFileText);

		nodeNotFoundNo =
				RubiLimitCommonComponent.createLabel(deleteNodeGroup, 140, "Node not found file count", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		rubiXmlDeleteNodeDTO.setNodeNotFoundNo(nodeNotFoundNo);
		nodeNotFoundNoText =
				RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, false, false, 5, 0);
		rubiXmlDeleteNodeDTO.setNodeNotFoundNoText(nodeNotFoundNoText);

		noOfScemaValidationFailed =
				RubiLimitCommonComponent.createLabel(deleteNodeGroup, 140, "Schema failed file count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
						body);
		rubiXmlDeleteNodeDTO.setNoOfScemaValidationFailed(noOfScemaValidationFailed);
		noOfScemaValidationFailedText =
				RubiLimitCommonComponent.createTextBox(toolkit, deleteNodeGroup, "", SWT.NONE, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
		rubiXmlDeleteNodeDTO.setNoOfScemaValidationFailedText(noOfScemaValidationFailedText);

		totalNumberOfXml.setFont(boldFont);
		noOfUpdatedFile.setFont(boldFont);
		nodeNotFoundNo.setFont(boldFont);
		noOfScemaValidationFailed.setFont(boldFont);
		return deleteNodeGroup;
	}

	// private static void createCommonComponent(Composite body, FormToolkit toolkit, Group insertNewNodeGroup) {
	// totalNumberOfXml =
	// RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Total No. Of Xml File:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
	// body);
	// // rubiXmlInsertNodeDTO.setTotalNumberOfXml(totalNumberOfXml);
	//	
	// totalNumberOfXmlText =
	// RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
	// // rubiXmlInsertNodeDTO.setTotalNumberOfXmlText(totalNumberOfXmlText);
	//	
	// noOfUpdatedFile =
	// RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Updated File count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0, body);
	// // rubiXmlInsertNodeDTO.setNoOfUpdatedFile(noOfUpdatedFile);
	//	
	// noOfUpdatedFileText =
	// RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
	// // rubiXmlInsertNodeDTO.setNoOfUpdatedFileText(noOfUpdatedFileText);
	//	
	// nodeNotFoundNo =
	// RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 130, "Node not found file count", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
	// body);
	// // rubiXmlInsertNodeDTO.setNodeNotFoundNo(nodeNotFoundNo);
	// nodeNotFoundNoText =
	// RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 100, SWT.LEFT, SWT.CENTER, false, false, 5, 0);
	// // rubiXmlInsertNodeDTO.setNodeNotFoundNoText(nodeNotFoundNoText);
	//	
	// noOfScemaValidationFailed =
	// RubiLimitCommonComponent.createLabel(insertNewNodeGroup, 120, "Schema failed file count:", SWT.LEFT, SWT.CENTER, false, false, 0, 0,
	// body);
	// // rubiXmlInsertNodeDTO.setNoOfScemaValidationFailed(noOfScemaValidationFailed);
	// noOfScemaValidationFailedText =
	// RubiLimitCommonComponent.createTextBox(toolkit, insertNewNodeGroup, "", SWT.BORDER, 100, SWT.LEFT, SWT.CENTER, true, false, 5, 0);
	// // rubiXmlInsertNodeDTO.setNoOfScemaValidationFailedText(noOfScemaValidationFailedText);
	//	
	// totalNumberOfXml.setVisible(false);
	// totalNumberOfXmlText.setVisible(false);
	// noOfUpdatedFile.setVisible(false);
	// noOfUpdatedFileText.setVisible(false);
	// nodeNotFoundNo.setVisible(false);
	// nodeNotFoundNoText.setVisible(false);
	// noOfScemaValidationFailed.setVisible(false);
	// noOfScemaValidationFailedText.setVisible(false);

	// }

	public void resetController() {
		rubiXmlCoreController.setTotalFileCounter(0);
		rubiXmlCoreController.setUpdatedFileCounter(0);
		rubiXmlCoreController.setNodeNotFoundFileCounter(0);
		rubiXmlCoreController.setInfectedFileSize(0);
	}

	public void resetScreenValues() {
		rubiXmlInsertNodeDTO.getInputDirText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlInsertNodeDTO.getRootNodeText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlInsertNodeDTO.getNewNodeNameText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlInsertNodeDTO.getXpathText().getText();
		rubiXmlInsertNodeDTO.getNodeValueText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlInsertNodeDTO.getAttributeNameText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlInsertNodeDTO.getAttributeValueText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlInsertNodeDTO.getInsertBeforeNodeText().setText(RubiLimitClientConstants.EMPTY_STRING);

		rubiXmlReplaceNodeDTO.getInputDirText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlReplaceNodeDTO.getOldNodeText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlReplaceNodeDTO.getXpathText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlReplaceNodeDTO.getNewNodeText().setText(RubiLimitClientConstants.EMPTY_STRING);

		rubiXmlReplaceNodeValueDTO.getInputDirText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlReplaceNodeValueDTO.getNodeNameText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlReplaceNodeValueDTO.getNodeTextValueText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlReplaceNodeValueDTO.getXpathText().setText(RubiLimitClientConstants.EMPTY_STRING);

		rubiXmlDeleteNodeDTO.getInputDirText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlDeleteNodeDTO.getNodeNameText().setText(RubiLimitClientConstants.EMPTY_STRING);
		rubiXmlDeleteNodeDTO.getXpathText().setText(RubiLimitClientConstants.EMPTY_STRING);
	}

	public void resetCommonComponent() {
		rubiXmlInsertNodeDTO.getTotalNumberOfXml().setVisible(false);
		rubiXmlInsertNodeDTO.getTotalNumberOfXmlText().setVisible(false);
		rubiXmlInsertNodeDTO.getNoOfScemaValidationFailedText().setVisible(false);
		rubiXmlInsertNodeDTO.getNoOfScemaValidationFailed().setVisible(false);
		rubiXmlInsertNodeDTO.getNodeNotFoundNo().setVisible(false);
		rubiXmlInsertNodeDTO.getNodeNotFoundNoText().setVisible(false);
		rubiXmlInsertNodeDTO.getNoOfUpdatedFile().setVisible(false);
		rubiXmlInsertNodeDTO.getNoOfUpdatedFileText().setVisible(false);
		rubiXmlDeleteNodeDTO.getTotalNumberOfXml().setVisible(false);
		rubiXmlDeleteNodeDTO.getTotalNumberOfXmlText().setVisible(false);
		rubiXmlDeleteNodeDTO.getNoOfScemaValidationFailedText().setVisible(false);
		rubiXmlDeleteNodeDTO.getNoOfScemaValidationFailed().setVisible(false);
		rubiXmlDeleteNodeDTO.getNodeNotFoundNo().setVisible(false);
		rubiXmlDeleteNodeDTO.getNodeNotFoundNoText().setVisible(false);
		rubiXmlDeleteNodeDTO.getNoOfUpdatedFile().setVisible(false);
		rubiXmlDeleteNodeDTO.getNoOfUpdatedFileText().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getTotalNumberOfXml().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getTotalNumberOfXmlText().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getNoOfScemaValidationFailedText().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getNoOfScemaValidationFailed().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getNodeNotFoundNo().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getNodeNotFoundNoText().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getNoOfUpdatedFile().setVisible(false);
		rubiXmlReplaceNodeValueDTO.getNoOfUpdatedFileText().setVisible(false);
		rubiXmlReplaceNodeDTO.getTotalNumberOfXml().setVisible(false);
		rubiXmlReplaceNodeDTO.getTotalNumberOfXmlText().setVisible(false);
		rubiXmlReplaceNodeDTO.getNoOfScemaValidationFailedText().setVisible(false);
		rubiXmlReplaceNodeDTO.getNoOfScemaValidationFailed().setVisible(false);
		rubiXmlReplaceNodeDTO.getNodeNotFoundNo().setVisible(false);
		rubiXmlReplaceNodeDTO.getNodeNotFoundNoText().setVisible(false);
		rubiXmlReplaceNodeDTO.getNoOfUpdatedFile().setVisible(false);
		rubiXmlReplaceNodeDTO.getNoOfUpdatedFileText().setVisible(false);
	}

	/**
	 * Gets the modified font data.
	 * 
	 * @param originalData
	 *            the original data
	 * @param additionalStyle
	 *            the additional style
	 * @return the modified font data
	 */
	private static FontData[] getModifiedFontData(final FontData[] originalData, final int additionalStyle) {
		final FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			final FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
		}
		return styleData;
	}

}