package game.ui;

import game.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class Menu extends UIElement implements MouseMotionListener, MouseListener {
    private List<Button> buttons;
    public int buttonWidth = 200;
    public int buttonHeight = 50;
    public int selectedButtonIndex = 0;
    public int spacing = 60;

    public Menu(int x, int y, int width, int height) {
        super(x, y, width, height, true);
        this.buttons = new ArrayList<>();
        initializeButtons();
    }

    public List<Button> getButtons()
    {
        return buttons;
    }

    public void initializeButtons() {
        this.logger.Log("Initalizing buttons");
    }

    public void setVisible(boolean visible)
    {
        for (Button button : this.getButtons())
        {
            button.setVisible(visible);
        }
    }

    public void keyPressed(int keyCode) {
        // Update selected button based on key press
        switch (keyCode) {
            case KeyEvent.VK_UP:
                buttons.get(selectedButtonIndex).setSelected(false);
                selectedButtonIndex = (selectedButtonIndex - 1 + buttons.size()) % buttons.size();
                buttons.get(selectedButtonIndex).setSelected(true);
                break;
            case KeyEvent.VK_DOWN:
                buttons.get(selectedButtonIndex).setSelected(false);
                selectedButtonIndex = (selectedButtonIndex + 1) % buttons.size();
                buttons.get(selectedButtonIndex).setSelected(true);
                break;
            case KeyEvent.VK_ENTER:
                selectOption();
                break;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        for (Button button : buttons) {
            if (button.containsPoint(e.getX(), e.getY())) {
                selectOption(button);
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void selectOption(Button button) {
        button.onOptionSelected();
    }

    private void selectOption() {
        Button selectedButton = buttons.get(selectedButtonIndex);
        logger.Log(String.format("Selected option %d: %s", selectedButtonIndex, selectedButton.getText()));
        selectedButton.onOptionSelected();
    }

    @Override
    public void draw(Graphics g) {
        for (Button button : buttons) {
            button.draw(g);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean isAnyButtonSelected = false;
        for (Button button : buttons) {
            if (button.containsPoint(e.getX(), e.getY())) {
                if (!button.isSelected) {
                    button.setSelected(true);
                }
                isAnyButtonSelected = true;
            } else {
                if (button.isSelected) {
                    button.setSelected(false);
                    // Optionally, repaint the menu or button here if needed
                }
            }
        }
        // If you want to deselect all buttons when not hovering over any button
        if (!isAnyButtonSelected) {
            deselectAllButtons();
        }
    }

    private void deselectAllButtons() {
        for (Button button : buttons) {
            button.setSelected(false);
        }
    }

}
