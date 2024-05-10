import javax.swing.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Test2 extends JFrame implements Runnable {
    public int FPS = 60;
    public GameField gameField;
    private JPanel panel;
    private Random rand = new Random();
    Thread gameThread;
    int minX;
    int minY;
    int maxX;
    int maxY;
    int width;
    int height;
    int type;
    long CONSTT = 1000000000;

    public static void main(String[] args) {

        new Test2(1440, 818, "Game", 350, 350, 1432, 789, 10, 120, 160, 4, 55, 14, 225, 180, 180, 215, 20, 1);

        // new Game(sizeNoInsetsFull().width, sizeNoInsetsFull().height,"Game",50,50,sizeNoInsetsFull().width-50,sizeNoInsetsFull().height-50,0,255,255,175,214,255,20);
        //   new Game(sizeNoInsetsFull().width, sizeNoInsetsFull().height,"Game",50,50,800,700,0,255,255,175,214,255,20);
        // new Game(1440, 789,"Game",0,0,1440,789,0,255,255,175,214,255,20);
    }

    public Test2(int w, int h, String title, int x1, int y1, int x2, int y2, int r, int g, int b, int window, int r1, int g1, int b1, int r2, int g2, int b2, int frameSize, int type) {
        setUndecorated(true);

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        size(w, h);
        //size(w,h);
        setLayout(null);
        //getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
        JPanel p = new JPanel();
        p.setBounds(0, 0, w, h);
        p.setBackground(Color.LIGHT_GRAY);
        gameField = new GameField(x1, y1, x2, y2, r1, g1, b1, r2, g2, b2, frameSize);
        width = gameField.x2Inner - gameField.x1Inner;
        height = gameField.y2Inner - gameField.y1Inner;
        minX = gameField.x1Inner - gameField.x1;
        minY = gameField.y1Inner - gameField.y1;
        maxX = minX + (gameField.x2Inner - gameField.x1Inner);
        maxY = minY + (gameField.y2Inner - gameField.y1Inner);
        this.type = type;

        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics gr) {
                Graphics2D graphics = (Graphics2D) gr;
                super.paintComponent(graphics);
                gameField.draw(graphics);

            }
        };

        panel.setBounds(x1, y1, x2 - x1, y2 - y1);

        getContentPane().add(p);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);

        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        MetalLookAndFeel.setCurrentTheme(new changeTheme(new Color(r, g, b)));

        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        getRootPane().setBorder(BorderFactory.createMatteBorder(window, window, window, window, new Color(r, g, b)));
        setVisible(true);
        startGameThread();

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        createList(type);
        double timePerFrame = CONSTT / FPS;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();
        while (gameThread != null) {
            now = System.nanoTime();

            if (now - lastFrame >= timePerFrame) {
                update(now);

                panel.repaint();
                lastFrame = now;
            }
        }
    }

    public void update(long t) {
        gameField.moveAllFigures(type, t);
        ArrayList<Circle> copy = new ArrayList<>(gameField.objects);

        for (Circle obj : copy) {
            if(obj.x2Frame >= maxX){
                obj.ang = 180 - obj.ang;
                obj.changeVector(t, "minus");
            } else if ( obj.y1Frame <= minY) {
                obj.ang = 270 - obj.ang;
                obj.changeVector(t, "minus");


            }

//                if(obj.x2Frame >= maxX || obj.y1Frame <= minY){
//                    System.out.println(obj.ang);
//                    if(obj.ang>180)
//                        obj.ang-=90;
//                    else
//                        obj.ang+=90;
//                    obj.changeVector(t, "minus");
//                } else {
//                    obj.changeVector(t, "plus");
//
//                }
        }
    }


    public Circle createFigure() {
        int rDraw = 0;
        int gDraw = 0;
        int bDraw = 0;
        int rFill = 255;
        int gFill = 255;
        int bFill = 255;
        int lineSize = 3;


        int radius = 35;
        int x1 = rand.nextInt(4 + minX, maxX - 2 * radius - 3);
        int y1 = rand.nextInt(4 + minY, maxY - 2 * radius - 3);
        Circle cir = new Circle(x1, y1, radius, rDraw, gDraw, bDraw, rFill, gFill, bFill, lineSize);
        return cir;

    }

    public void createList(int type) {
       // for(int i=0; i<10; i++){
            Circle cir = createFigure();

            gameField.addToMove(cir, type);
       // }


    }

    public Dimension size(int w, int h) {
        Dimension dim = new Dimension();
        dim.width = w;
        dim.height = h;
        this.setPreferredSize(dim);
        this.pack();
        int realW = this.getContentPane().getWidth();
        int realH = this.getContentPane().getHeight();
        System.out.println(realW + " " + realH);
        dim.width = 2 * dim.width - realW;
        dim.height = 2 * dim.height - realH;
        this.setPreferredSize(dim);
        System.out.println(dim.width + " " + dim.height);
        System.out.println(this.getContentPane().getSize().getHeight());
        this.pack();
        return dim;
    }


}

