package magic.ui.duel.choice;

import magic.ui.IconImages;
import magic.model.MagicSource;
import magic.ui.SwingGameController;
import magic.ui.duel.viewer.UserActionPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TextLabel;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import magic.data.MagicIcon;
import magic.model.IGameController;

public class MayChoicePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Dimension BUTTON_DIMENSION=new Dimension(100,35);

    private final SwingGameController controller;
    private final JButton yesButton;
    private boolean yes;

    public MayChoicePanel(final IGameController controllerObj,final MagicSource source,final String message) {

        this.controller = (SwingGameController) controllerObj;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,message),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.CENTER);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        add(buttonPanel,BorderLayout.SOUTH);

        yesButton=new JButton("Yes",IconImages.getIcon(MagicIcon.OK));
        yesButton.setPreferredSize(BUTTON_DIMENSION);
        yesButton.addActionListener(this);
        yesButton.setFocusable(false);
        buttonPanel.add(yesButton);

        yesButton.getInputMap(2).put(KeyStroke.getKeyStroke('y'),"yes");
        yesButton.getActionMap().put("yes",new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(final ActionEvent evt) {
                yes = true;
                controller.actionClicked();
            }
        });


        final JButton noButton=new JButton("No",IconImages.getIcon(MagicIcon.CANCEL));
        noButton.setPreferredSize(BUTTON_DIMENSION);
        noButton.addActionListener(this);
        noButton.setFocusable(false);
        buttonPanel.add(noButton);

        noButton.getInputMap(2).put(KeyStroke.getKeyStroke('n'),"no");
        noButton.getActionMap().put("no",new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(final ActionEvent evt) {
                yes = false;
                controller.actionClicked();
            }
        });
    }

    public boolean isYesClicked() {
        return yes;
    }
    protected void setYesClicked(final boolean b) {
        yes = b;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        yes = event.getSource() == yesButton;
        controller.actionClicked();
    }

    public SwingGameController getGameController() {
        return controller;
    }

}
