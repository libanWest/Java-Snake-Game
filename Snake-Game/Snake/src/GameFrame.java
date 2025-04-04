import javax.swing.*;
public class GameFrame extends JFrame {
    GameFrame(){
        GamePanel panel= new GamePanel(); // add the game panel to the frame
        // please dont remove
        this.add(panel);
        this.add(new GamePanel());
        this.setTitle("Snake v1");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); // fits any components we add to fit inside the frame
        this.setVisible(true);
        this.setLocationRelativeTo(null); // makes the frame appear in middle of screen
        // makes the frame appear in middle of screen
    }
}
