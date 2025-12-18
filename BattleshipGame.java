import java.util.Scanner;

public class BattleshipGame {
    // Sabitler: Tahta boyutu ve semboller
    private static final int BOARD_SIZE = 10;
    private static final char WATER = '.';          // Su (Boş alan)
    private static final char SHIP_HIDDEN = '#';    // Gemi (Gizli)
    private static final char HIT = 'X';            // İsabet
    private static final char MISS = 'O';           // Iskalama

    // Oyuncu tahtaları
    private char[][] player1Board = new char[BOARD_SIZE][BOARD_SIZE];
    private char[][] player2Board = new char[BOARD_SIZE][BOARD_SIZE];
    
    // Gemi bilgisi: İki adet 3 birim gemi
    private final int[] SHIP_SIZES = {3, 3}; 
    private final int TOTAL_SHIP_PARTS = 6; 

    // İsabet Sayıları
    private int player1Hits = 0; // P1'in P2 tahtasına yaptığı isabet
    private int player2Hits = 0; // P2'nin P1 tahtasına yaptığı isabet

    private Scanner scanner = new Scanner(System.in);

    /**
     * Oyunun ana başlangıç metodu.
     */
    public void start() {
        System.out.println("--------------------------------------------------");
        System.out.println("🌊⚓ Java BASİT İki Oyunculu Amiral Battı ⚓🚢");
        System.out.println("--------------------------------------------------");
        System.out.println("Oyun 10x10 tahtada, her oyuncunun 2 adet 3 birimlik gemisi ile oynanacaktır.");
        
        // Tahtaları başlangıçta su ile doldur (Boş tahta)
        initializeBoard(player1Board);
        initializeBoard(player2Board);
        
        // 1. Oyuncuların gemileri yerleştirmesi
        playerPlacement(1, player1Board); 
        playerPlacement(2, player2Board); 
        
        System.out.println("\n--------------------------------------------------");
        System.out.println("Oyun Başlıyor! İyi şanslar!");
        System.out.println("--------------------------------------------------");
        sleep(1000); // Başlangıç mesajından sonra bekle
        
        // 2. Oyun döngüsü: Atışlar başlar
        gameLoop();
        
        // 3. Oyun bittiğinde kazananı göster
        displayGameOverMessage();
    }

    /**
     * Verilen tahtayı '.' (su) sembolü ile doldurur.
     */
    private void initializeBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = WATER;
            }
        }
    }

    /**
     * Oyuncuların gemilerini sırayla yerleştirmesini sağlar.
     */
    private void playerPlacement(int playerNum, char[][] board) {
        System.out.println("\n--- Oyuncu " + playerNum + " Gemi Yerleştirme ---");
        
        for (int size : SHIP_SIZES) {
            boolean placed = false;
            while (!placed) {
                // Oyuncunun kendi tahtasını göster (gemiler görünüyor)
                displayBoardForPlacement(board); 

                System.out.println("Yerleştirilecek Gemi Boyutu: " + size + " birim.");
                System.out.print("Başlangıç Satırı (1-10): ");
                int startRow = getValidInput(1, BOARD_SIZE) - 1; // 0-9'a çevir

                System.out.print("Başlangıç Sütunu (1-10): ");
                int startCol = getValidInput(1, BOARD_SIZE) - 1; // 0-9'a çevir

                System.out.print("Yön (Y: Yatay, D: Dikey): ");
                String directionInput = scanner.next().toUpperCase();
                int direction = -1; // 0: Yatay, 1: Dikey

                if (directionInput.startsWith("Y")) {
                    direction = 0;
                } else if (directionInput.startsWith("D")) {
                    direction = 1;
                } else {
                    System.out.println("❌ Hata! Geçersiz yön. Lütfen 'Y' veya 'D' girin.");
                    continue;
                }

                // Gemi yerleştirme geçerliliğini kontrol et
                if (isPlacementValid(startRow, startCol, size, direction, board)) {
                    // Geçerliyse gemiyi yerleştir (Tahtayı '#' ile işaretle)
                    for (int i = 0; i < size; i++) {
                        if (direction == 0) { // Yatay yerleştirme
                            board[startRow][startCol + i] = SHIP_HIDDEN;
                        } else { // Dikey yerleştirme
                            board[startRow + i][startCol] = SHIP_HIDDEN;
                        }
                    }
                    placed = true;
                    System.out.println("✅ Gemi başarıyla yerleştirildi.");
                    sleep(1000); // Gemi yerleştirildikten sonra bekle
                } else {
                    System.out.println("❌ Hata! Gemi sınır dışına taşıyor veya başka bir geminin üzerine yerleşiyor. Tekrar deneyin.");
                }
            }
        }
    }

    /**
     * Bir geminin tahta sınırları içinde ve başka bir gemiyle çakışmayacak 
     * şekilde yerleştirilip yerleştirilemeyeceğini kontrol eder.
     * Basitleştirilmiş versiyon: Bitişiklik kontrolü yapılmaz.
     */
    private boolean isPlacementValid(int startRow, int startCol, int size, int direction, char[][] board) {
        // 1. Sınır kontrolü
        if (direction == 0) { // Yatay
            if (startCol + size > BOARD_SIZE) return false;
        } else { // Dikey
            if (startRow + size > BOARD_SIZE) return false;
        }

        // 2. Çakışma (Üst Üste Binme) kontrolü
        for (int i = 0; i < size; i++) {
            int row = startRow + (direction == 1 ? i : 0);
            int col = startCol + (direction == 0 ? i : 0);
            
            // Eğer o noktada zaten bir gemi varsa
            if (board[row][col] == SHIP_HIDDEN) {
                return false; 
            }
        }

        return true;
    }

    /**
     * Oyunun temel sıra tabanlı döngüsünü çalıştırır.
     */
    private void gameLoop() {
        int currentPlayer = 1;
        while (player1Hits < TOTAL_SHIP_PARTS && player2Hits < TOTAL_SHIP_PARTS) {
            
            // Sıra kimde? Hangi tahtaya atış yapacak?
            char[][] targetBoard = (currentPlayer == 1) ? player2Board : player1Board;
            String playerName = "Oyuncu " + currentPlayer;

            System.out.println("\n--------------------------------------------------");
            System.out.println("💣 Sıra: " + playerName);
            // Karşı tarafın kalan gemi parçasını göster
            int remainingParts = TOTAL_SHIP_PARTS - ((currentPlayer == 1) ? player1Hits : player2Hits);
            System.out.println("Hedef Tahtası (Kalan Gemi Parçası: " + remainingParts + "):");
            
            // Oyuncunun gördüğü hedef tahtayı göster (Gemiler gizli)
            displayBoardForAttack(targetBoard); 

            int row = -1, col = -1;
            boolean validInput = false;

            while (!validInput) {
                System.out.print(playerName + " Atış Satırı (1-10): ");
                row = getValidInput(1, BOARD_SIZE) - 1; // 0-9'a çevir
                
                System.out.print(playerName + " Atış Sütunu (1-10): ");
                col = getValidInput(1, BOARD_SIZE) - 1; // 0-9'a çevir

                // Daha önce atış yapılıp yapılmadığını kontrol et
                if (targetBoard[row][col] == HIT || targetBoard[row][col] == MISS) {
                    System.out.println("❌ Hata! Buraya daha önce atış yaptınız. Tekrar deneyin.");
                } else {
                    validInput = true; // Giriş geçerli
                }
            }
            
            // Atışı tahtada işle
            processShot(currentPlayer, targetBoard, row, col);

            // Sıra değiştirme
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }
    }

    /**
     * Yapılan atışı tahtada işler ve oyuncuya geri bildirim verir.
     */
    private void processShot(int playerNum, char[][] targetBoard, int row, int col) {
        if (targetBoard[row][col] == SHIP_HIDDEN) {
            // İsabet!
            targetBoard[row][col] = HIT;
            if (playerNum == 1) {
                player1Hits++;
            } else {
                player2Hits++;
            }
            
            System.out.println("\n💥 EYVAH! BİR GEMİYE İSABET ETTİ!");
            sleep(1000); // İsabetten sonra bekle
        } else if (targetBoard[row][col] == WATER) {
            // Iskalama
            targetBoard[row][col] = MISS;
            System.out.println("\n🌊 ISKALAMA! Suya düştü.");
            sleep(1000); // Iskalamadan sonra bekle
        }
    }

    /**
     * Yerleştirme sırasında oyuncunun kendi tahtasını gösterir (Gemiler görünür).
     */
    private void displayBoardForPlacement(char[][] board) {
        // Tahta başlıkları
        System.out.print("\n   ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%3d", i + 1);
        }
        System.out.println();
        
        // Tahta içeriği
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%2d ", i + 1); // Satır numarası
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf("%3c", board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Atış sırasında hedef tahtayı gösterir (Gemiler gizli).
     */
    private void displayBoardForAttack(char[][] board) {
        // Tahta başlıkları
        System.out.print("\n   ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%3d", i + 1);
        }
        System.out.println();
        
        // Tahta içeriği
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%2d ", i + 1); // Satır numarası
            for (int j = 0; j < BOARD_SIZE; j++) {
                char displayChar = board[i][j];
                
                if (displayChar == SHIP_HIDDEN) {
                    // Atış sırasında gizli gemi parçalarını su olarak göster
                    displayChar = WATER;
                }
                
                System.out.printf("%3c", displayChar);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Kullanıcıdan geçerli (min ve max arasında) sayısal giriş alır.
     * Hatalı giriş durumunda döngüye devam eder.
     */
    private int getValidInput(int min, int max) {
        while (true) {
            try {
                // Eğer scanner.hasNextInt() kullanırsak, hatalı input'u tüketmemiz gerekir.
                // scanner.nextInt() kullanıp try/catch bloğu ile hatalı girişi yönetelim.
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("❌ Hata! Lütfen " + min + " ile " + max + " arasında bir sayı girin.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("❌ Hata! Lütfen sadece sayı girin.");
                scanner.next(); // Hatalı (sayı olmayan) girişi temizle
            }
        }
    }
    
    /**
     * Programı belirli bir süre (milisaniye cinsinden) durdurur.
     * Bu metot, çıktının kullanıcı tarafından okunabilmesi için bekleme sağlar.
     */
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Eğer uyku kesintiye uğrarsa, iş parçacığının kesinti durumunu tekrar ayarla.
            Thread.currentThread().interrupt(); 
        }
    }

    /**
     * Oyun sonu mesajını görüntüler ve kazananı ilan eder.
     */
    private void displayGameOverMessage() {
        int winner = (player1Hits == TOTAL_SHIP_PARTS) ? 1 : 2;
        int loser = (winner == 1) ? 2 : 1;
        
        System.out.println("\n--------------------------------------------------");
        System.out.println("⚔️ OYUN BİTTİ!");
        sleep(1000); // Oyun bitti mesajından sonra bekle
        System.out.println("🎉 TEBRİKLER! Oyuncu " + winner + " KAZANDI!");
        sleep(1000); // Kazanan ilanından sonra bekle
        
        System.out.println("\n--- Oyuncu 1'in Tahtası (Son Durum) ---");
        displayBoardForPlacement(player1Board); 
        
        System.out.println("\n--- Oyuncu 2'nin Tahtası (Son Durum) ---");
        displayBoardForPlacement(player2Board); 
        
        System.out.println("--------------------------------------------------");
    }

    // Programın çalışmaya başladığı ana metot
    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame();
        game.start();
    }
}