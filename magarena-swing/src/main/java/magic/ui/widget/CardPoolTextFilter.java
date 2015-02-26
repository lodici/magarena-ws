package magic.ui.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import magic.ui.explorer.ExplorerDeckEditorPanel;

import magic.ui.widget.TextPrompt.Show;

@SuppressWarnings("serial")
public class CardPoolTextFilter extends JTextField implements DocumentListener {

    private static final int SEARCH_TIMER_DELAY = 500; //msecs

    private Timer searchTextTimer;
    private final Font defaultFont;
    private final Font searchingFont;
    private final ExplorerDeckEditorPanel explorerPanel;

    private final List<String> searchTerms = new ArrayList<>();

    public CardPoolTextFilter(final ExplorerDeckEditorPanel explorerPanel) {

        this.explorerPanel = explorerPanel;
        defaultFont = getFont();
        searchingFont = defaultFont.deriveFont(Font.BOLD);

        setLookAndFeel();

        getDocument().addDocumentListener(this);
    }

    private void setLookAndFeel() {
        final TextPrompt promptField = new TextPrompt("Search text...", this);
        promptField.setShow(Show.FOCUS_LOST);
        promptField.changeStyle(Font.ITALIC);
        promptField.setForeground(Color.GRAY);
        promptField.setFocusable(false);
    }

    @Override
    public void changedUpdate(DocumentEvent e) { }

    @Override
    public void insertUpdate(DocumentEvent e) {
        startSearchTextTimer();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        startSearchTextTimer();
    }

    private void createSearchTextTimer() {
        if (searchTextTimer == null) {
            searchTextTimer = new Timer(SEARCH_TIMER_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchTextTimer.stop();
                    explorerPanel.updateCardPool();
                    setFont(defaultFont);
                }
            });
        }
    }

    private void startSearchTextTimer() {
        setListOfSearchTerms();
        createSearchTextTimer();
        searchTextTimer.setInitialDelay(SEARCH_TIMER_DELAY);
        searchTextTimer.restart();
        setFont(searchingFont);
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        if (t.isEmpty()) {
            searchTerms.clear();
        }
    }

    private void setListOfSearchTerms() {
        searchTerms.clear();
        if (!getText().isEmpty()) {
            // extract words or phrases (delimited by double-quotes).
            Pattern regex = Pattern.compile("\"([^\"]+)\"|(\\S+)");
            Matcher matcher = regex.matcher(getText());
            while (matcher.find()) {
                // remove delimiting quotes from any phrase.
                final String matched = matcher.group().replaceAll("^\"|\"$", "");
                searchTerms.add(matched);
//                System.out.println("matched = \"" + matched + "\"");
            }
        }
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

}
