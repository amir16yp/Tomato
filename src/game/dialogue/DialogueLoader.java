package game.dialogue;

import game.DefaultResourceLoader;
import game.Logger;
import game.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class DialogueLoader {

    private static Logger logger = new Logger(DialogueNode.class.getName());

    public static DialogueNode loadDialogueTree(String xmlFilePath, ResourceLoader resourceLoader) {
        logger.Log("Loading from " + xmlFilePath);
        try {
            // Create document builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse XML file
            if (resourceLoader == null)
            {
                resourceLoader = new DefaultResourceLoader();
            }
            Document document = builder.parse(String.join("\n", resourceLoader.loadTextFile(xmlFilePath)));
            // Get root element
            Element rootElement = document.getDocumentElement();

            // Parse dialogue tree
            return parseDialogueNode(rootElement);
        } catch (Exception e) {
            logger.Error(e);
            return null;
        }
    }

    private static DialogueNode parseDialogueNode(Element element) {
        // Get message
        String message = element.getElementsByTagName("message").item(0).getTextContent();
        DialogueNode node = new DialogueNode(message);

        // Get options
        NodeList optionsList = element.getElementsByTagName("option");
        for (int i = 0; i < optionsList.getLength(); i++) {
            Node optionNode = optionsList.item(i);
            if (optionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element optionElement = (Element) optionNode;
                String optionText = optionElement.getElementsByTagName("text").item(0).getTextContent();
                DialogueNode nextNode = parseDialogueNode((Element) optionElement.getElementsByTagName("nextNode").item(0));
                node.addOption(new DialogueOption(optionText, nextNode));
            }
        }

        return node;
    }
}
