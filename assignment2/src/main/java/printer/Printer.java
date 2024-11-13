package printer;

import java.util.ArrayList;

public class Printer {
  ArrayList<String> queue = new ArrayList<>();

  void queue(String filename) {
    queue.add(filename);
  }

}
