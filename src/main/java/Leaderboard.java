import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;

public class Leaderboard {
    private static final int MAX_ENTRIES = 10;
    private static final String KEY = "scores";
    private static final Preferences PREFS =
            Preferences.userRoot().node("irmakonder/tic-tac-toe-minimax");

    private final List<ScoreEntry> entries = new ArrayList<>();

    public Leaderboard() {
        load();
    }

    public List<ScoreEntry> getEntries() {
        return entries;
    }

    public void add(ScoreEntry entry) {
        entries.add(0, entry);
        entries.sort(Comparator.comparingInt(ScoreEntry::points).reversed());
        while (entries.size() > MAX_ENTRIES) {
            entries.remove(entries.size() - 1);
        }
        save();
    }

    private void load() {
        String data = PREFS.get(KEY, "");
        if (data.isEmpty()) {
            return;
        }
        for (String line : data.split("\n")) {
            String[] parts = line.split(";");
            if (parts.length == 3) {
                try {
                    entries.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1]), parts[2]));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping broken score line: " + line);
                }
            }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (ScoreEntry e : entries) {
            lines.add(e.name() + ";" + e.points() + ";" + e.mode());
        }
        PREFS.put(KEY, String.join("\n", lines));
    }
}