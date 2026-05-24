/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package demo;

/**
 *
 * @author pecen
 */
public class Main_medium extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main_medium.class.getName());

    /**
     * Creates new form Main_srednje
     */
    private int x;
    private int y;
    private int speed = 5;
    private boolean left = false;
    private boolean right = false;
    private javax.swing.Timer timer;
    private javax.swing.ImageIcon idleLeft;
    private javax.swing.ImageIcon idleRight;
    private javax.swing.ImageIcon runLeft;
    private javax.swing.ImageIcon runRight;
    private javax.swing.ImageIcon jumpLeft;
    private javax.swing.ImageIcon jumpRight;
    private boolean facingRight = true;
    private boolean jumping = false;
    private boolean napad = false;
    private double velocityY = 0;
    private double gravity = 0.7;
    private double jumpStrength = -10;
    private java.util.List<javax.swing.JLabel> padajociObjekti = new java.util.ArrayList<>();
    private int spawnTimer = 0;
    private String[] slikePredmetov = {
    "/slike/Modri_Gem.png", 
    "/slike/Rumeni_Gem.png", 
    "/slike/Zeleni_Gem.png",
    "/slike/Rdeci_Gem.png",
    "/slike/Kamen.png",
    "/slike/Kamen.png"
    };
    private java.util.Random random = new java.util.Random();
    private int razdaljaOdZadnjega = 0; 
    private final int MIN_RAZDALJA = 1;
    private int hitrostPajka = 4;
    private int tocke = 0;
    private int zivljenja = 3;
    private boolean vStikuSPajkom = false;
    private java.util.List<javax.swing.JLabel> nabojeList = new java.util.ArrayList<>();
    private int hitrostMetka = 12;
    private long zadnjiStrel = 0;
    private long zacetniCas;
    private int stopnja = 2;

    public Main_medium() {
        initComponents();
        zacetniCas = System.currentTimeMillis();
        jLabel7.setVisible(false);
        java.awt.EventQueue.invokeLater(() -> {
            x = jLabel2.getX();
            y = jLabel2.getY();
        });
        idleLeft = new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Idle_Left.gif"));
        idleRight = new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Idle.gif"));
        runLeft = new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Run_Left.gif"));
        runRight = new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Run_Right.gif"));
        jumpLeft = new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Jump_Left.gif"));
        jumpRight = new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Jump_Right.gif"));
        java.awt.EventQueue.invokeLater(() -> {
            jLabel4.setLocation(getContentPane().getWidth() + 100, 313);
        });
        nastaviTipke();
        zazeniTimer();
    }
    
    private void nastaviTipke() {
        javax.swing.InputMap im = getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap am = getRootPane().getActionMap();
        im.put(javax.swing.KeyStroke.getKeyStroke("pressed A"), "levoPritisnjeno");
        im.put(javax.swing.KeyStroke.getKeyStroke("released A"), "levoSpusceno");
        im.put(javax.swing.KeyStroke.getKeyStroke("pressed D"), "desnoPritisnjeno");
        im.put(javax.swing.KeyStroke.getKeyStroke("released D"), "desnoSpusceno");
        im.put(javax.swing.KeyStroke.getKeyStroke("pressed SPACE"), "jumpPress");
        im.put(javax.swing.KeyStroke.getKeyStroke("pressed ENTER"), "ustreli");
        am.put("levoPritisnjeno", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                left = true;
            }
        });

        am.put("levoSpusceno", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                left = false;
            }
        });

        am.put("desnoPritisnjeno", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                right = true;
            }
        });

        am.put("desnoSpusceno", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                right = false;
            }
        });
        
        am.put("jumpPress", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!jumping) {
                    jumping = true;
                    velocityY = jumpStrength;
                }
            }
        });
        
        am.put("ustreli", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (System.currentTimeMillis() - zadnjiStrel > 5000) { // Vsaj pol sekunde med streli
                    ustvariMetek();
                    zadnjiStrel = System.currentTimeMillis();
                }
            }
        });
    }

    private void zazeniTimer() {
        timer = new javax.swing.Timer(16, e -> {
                long trenutniCas = System.currentTimeMillis();
                int pretekleSekunde = (int) ((trenutniCas - zacetniCas) / 1000);
                
                if (pretekleSekunde >= 60) {
                    timer.stop();
                    dispose();
                    new End(tocke, pretekleSekunde, stopnja).setVisible(true);
                    return; // Prekinemo izvajanje preostale logike v tem koraku
                }
                boolean moving = false;
                if (right && x + jLabel2.getWidth() < getContentPane().getWidth()) {
                    x += speed;
                    facingRight = true;
                    moving = true;
                }
                if (left && x > 0) {
                    x -= speed;
                    facingRight = false;
                    moving = true;
                }
                if (jumping) {
                    velocityY += gravity;
                    y += velocityY;

                    if (y >= 220) {
                        y = 220;
                        jumping = false;
                        velocityY = 0;
                    }
                }
                if (jumping) {
                    jLabel2.setIcon(facingRight ? jumpRight : jumpLeft);
                } else if (moving) {
                    jLabel2.setIcon(facingRight ? runRight : runLeft);
                } else {
                    jLabel2.setIcon(facingRight ? idleRight : idleLeft);
                }
                jLabel2.setLocation(x, y);
                razdaljaOdZadnjega += 3;
                if (padajociObjekti.size() < 1 && razdaljaOdZadnjega >= MIN_RAZDALJA) {
                    ustvariPadajociObjekt();
                    razdaljaOdZadnjega = 0; 
                }
                int noviX = jLabel4.getX() - hitrostPajka;
                jLabel4.setLocation(noviX, jLabel4.getY());
                if (getContentPane().getLayout() instanceof org.netbeans.lib.awtextra.AbsoluteLayout) {
                    ((org.netbeans.lib.awtextra.AbsoluteLayout)getContentPane().getLayout()).addLayoutComponent(
                        jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(noviX, jLabel4.getY(), jLabel4.getWidth(), jLabel4.getHeight())
                    );
                }
                if (jLabel4.getX() + jLabel4.getWidth() < 0) {
                    int zacetniX = getContentPane().getWidth() + random.nextInt(200);
                    jLabel4.setLocation(zacetniX, jLabel4.getY());
                }            
                if (jLabel4.getBounds().intersects(jLabel2.getBounds())) {
                    if (!vStikuSPajkom) {
                        zivljenja -= 1;
                        jLabel5.setText("Lives: " + zivljenja);
                        vStikuSPajkom = true;

                        // --- DODANO: Resetiraj pajka na začetek takoj ob dotiku ---
                        int resetX = getContentPane().getWidth() + random.nextInt(200);
                        jLabel4.setLocation(resetX, jLabel4.getY());

                        // Posodobimo še AbsoluteLayout, če ga uporabljaš
                        if (getContentPane().getLayout() instanceof org.netbeans.lib.awtextra.AbsoluteLayout) {
                            ((org.netbeans.lib.awtextra.AbsoluteLayout)getContentPane().getLayout()).addLayoutComponent(
                                jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(resetX, jLabel4.getY(), jLabel4.getWidth(), jLabel4.getHeight())
                            );
                        }
                        // ---------------------------------------------------------

                        if (zivljenja <= 0) {
                            timer.stop();
                            long končniCas = System.currentTimeMillis();
                            int preziveteSekunde = (int) ((končniCas - zacetniCas) / 1000);
                            System.out.println("Preživel si: " + preziveteSekunde + " sekund.");

                            dispose();
                            new End(tocke, pretekleSekunde, stopnja).setVisible(true);
                        }
                    }
                } else {
                    vStikuSPajkom = false;
                }
                for (int i = 0; i < padajociObjekti.size(); i++) {
                    javax.swing.JLabel obj = padajociObjekti.get(i);
                    obj.setLocation(obj.getX(), obj.getY() + 3); 

                    if (jLabel2.getBounds().intersects(obj.getBounds())) {
                        String trenutnaSlika = obj.getIcon().toString();

                        if (trenutnaSlika.contains("Kamen.png")) {
                            tocke -= 10;
                        } else {
                            tocke += 10;
                        }

                        // Posodobimo izpis na zaslonu
                        jLabel6.setText("Score: " + tocke); // Nastavi barvo, da bo vidna

                        // Odstranimo objekt
                        obj.setVisible(false);
                        obj.setLocation(obj.getX(), -500);
                        padajociObjekti.remove(i);
                        i--;
                        continue;
                    }

                        // Odstranjevanje, če objekt pade mimo dna
                        if (obj.getY() > getContentPane().getHeight()) {
                            obj.setVisible(false);
                            padajociObjekti.remove(i);
                            i--;
                        }
                }
                // Premikanje vseh izstreljenih metkov
                for (int i = 0; i < nabojeList.size(); i++) {
                    javax.swing.JLabel m = nabojeList.get(i);
                    int trenutniX = m.getX();

                    if ("DESNO".equals(m.getName())) {
                        m.setLocation(trenutniX + hitrostMetka, m.getY());
                    } else {
                        m.setLocation(trenutniX - hitrostMetka, m.getY());
                    }

                    // TRK S PAJKOM (jLabel4)
                    if (m.getBounds().intersects(jLabel4.getBounds())) {
                        // 1. Določi novo pozicijo (desno izven zaslona)
                        int resetX = getContentPane().getWidth() + 200;
                        int resetY = jLabel4.getY();

                        // 2. NUJNO za AbsoluteLayout: Posodobi layout komponento
                        if (getContentPane().getLayout() instanceof org.netbeans.lib.awtextra.AbsoluteLayout) {
                            ((org.netbeans.lib.awtextra.AbsoluteLayout)getContentPane().getLayout()).addLayoutComponent(
                                jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(resetX, resetY, jLabel4.getWidth(), jLabel4.getHeight())
                            );
                        }

                        // 3. Premakni vizualno
                        jLabel4.setLocation(resetX, resetY);

                        // 4. Točke in uničenje metka
                        tocke += 10;
                        jLabel6.setText("Score: " + tocke);
                        uničiMetek(m, i);
                        i--;

                        getContentPane().repaint(); // Osveži zaslon
                        continue; 
                    }

                    // Odstrani metek izven zaslona
                    if (trenutniX > getContentPane().getWidth() || trenutniX < -50) {
                        uničiMetek(m, i);
                        i--;
                    }
                }
            });
            timer.start();
    }

    private void ustvariPadajociObjekt() {
        String izbranaSlika = slikePredmetov[random.nextInt(slikePredmetov.length)];
        try {
            java.net.URL imgURL = getClass().getResource(izbranaSlika);
            if (imgURL != null) {
                javax.swing.ImageIcon ikona = new javax.swing.ImageIcon(imgURL);
                jLabel3.setIcon(ikona);
                int sirina = ikona.getIconWidth();
                int visina = ikona.getIconHeight();
                jLabel3.setSize(sirina, visina);
                int maxSirina = getContentPane().getWidth() - sirina;
                int nakljucniX = random.nextInt(Math.max(1, maxSirina));
                jLabel3.setLocation(nakljucniX, -visina);
                if (getContentPane().getLayout() instanceof org.netbeans.lib.awtextra.AbsoluteLayout) {
                    ((org.netbeans.lib.awtextra.AbsoluteLayout)getContentPane().getLayout()).addLayoutComponent(
                        jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(nakljucniX, -visina, sirina, visina)
                    );
                }
                if (!padajociObjekti.contains(jLabel3)) {
                    padajociObjekti.add(jLabel3);
                }
                jLabel3.setVisible(true);
                getContentPane().setComponentZOrder(jLabel3, 0); 
                getContentPane().repaint();
            }
        } catch (Exception ex) {
            System.err.println("Napaka pri nastavljanju slike: " + izbranaSlika);
        }
    }
    
    private void ustvariMetek() {
        // POZOR: Zamenjaj 'jLabelMetek' z imenom tvojega JLabela iz GUI (npr. jLabel7)
        javax.swing.JLabel novMetek = new javax.swing.JLabel(jLabel7.getIcon());
        novMetek.setSize(jLabel7.getWidth(), jLabel7.getHeight());

        // Metek naj se pojavi pri igralcu
        int mX = facingRight ? (x + jLabel2.getWidth()) : (x - novMetek.getWidth());
        int mY = y + jLabel2.getHeight() - 30;

        novMetek.setLocation(mX, mY);
        novMetek.setName(facingRight ? "DESNO" : "LEVO"); // Shranimo smer v ime

        // Dodajanje na zaslon
        getContentPane().add(novMetek, new org.netbeans.lib.awtextra.AbsoluteConstraints(mX, mY, novMetek.getWidth(), novMetek.getHeight()));
        nabojeList.add(novMetek);

        getContentPane().setComponentZOrder(novMetek, 0);
        novMetek.setVisible(true);
    }
    
    private void uničiMetek(javax.swing.JLabel m, int index) {
        m.setVisible(false);
        getContentPane().remove(m);
        nabojeList.remove(index);
        getContentPane().repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MEDIUM MODE");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/slike/18-ezgif.com-resize.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, -1, -1));

        jLabel6.setFont(new java.awt.Font("Algerian", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("SCORE: 0");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 140, 30));

        jLabel5.setFont(new java.awt.Font("Algerian", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("LIVES: 3");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 110, 30));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/slike/dd.gif"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 320, -1, -1));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/slike/Character_Idle.gif"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 60, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/slike/uvodna.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jMenu1.setText("HOW TO PLAY");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("ABOUT");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        jMenu3.setText("EXIT");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(728, 434));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        new How_to_play().setVisible(true);
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        new About().setVisible(true);
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        int odgovor = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "Ali ste prepričani, da želite zapustiti igro?", 
            "Izhod", 
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );
        if (timer != null) {
            timer.stop(); // Ustaviš izvajanje vseh dogodkov v igri
        }
        if (odgovor == javax.swing.JOptionPane.YES_OPTION) {
            dispose();
            new Main_page().setVisible(true);
        }
    }//GEN-LAST:event_jMenu3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Main_medium().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
}
