import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Pilove {

    public static double fiyatHesapla(double birimFiyat, double miktar) {
        return birimFiyat * miktar;
    }

    public static void kucukGecikme(long milisaniye) {
        try {
            Thread.sleep(milisaniye);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void menuGoster(String baslik, String[] secenekler, double[] fiyatlar) {
        kucukGecikme(500);
        System.out.println("\n--- " + baslik + " ---");
        for (int i = 0; i < secenekler.length; i++) {
            System.out.println((i + 1) + " )- " + secenekler[i] + " (" + fiyatlar[i] + " TL)");
        }
    }

    public static int secimAl(Scanner scanner) {
        int secim;
        do {
            System.out.print("Seciminiz (1-4): ");
            secim = scanner.nextInt();
            if (secim < 1 || secim > 4) System.out.println("Gecersiz secim! Lutfen listeden secin.");
        } while (secim < 1 || secim > 4);
        return secim;
    }

    public static double miktarAlVeStoktanDus(Scanner scanner, double[] stoklar, int secim, String birim) {
        double miktar;
        do {
            System.out.print("Kac " + birim + " istersiniz? : ");
            miktar = scanner.nextDouble();
            if (miktar > stoklar[secim - 1]) {
                System.out.println("Hata: Stok yetersiz! Mevcut stok: " + stoklar[secim-1]);
            }
        } while (miktar > stoklar[secim - 1]);
        
        stoklar[secim - 1] -= miktar;
        return miktar;
    }

    public static void dosyayaKaydet(String ad, String tel, String adres, String urun, double uPors, double uTutar, String icecek, double iPors, double iTutar, double indirim, double toplam) {
        try (PrintWriter yazici = new PrintWriter(new FileWriter("siparis_gecmisi.txt", true))) {
            yazici.println("--- YENI SIPARIS ---");
            yazici.println("Musteri: " + ad + " | Tel: " + tel);
            yazici.println("Adres: " + adres);
            yazici.println("Yemek: " + urun + " x " + uPors + " pors. = " + uTutar + " TL");
            yazici.println("Icecek: " + icecek + " x " + iPors + " adet = " + iTutar + " TL");
            if (indirim > 0) yazici.println("Indirim (PILOVE10): -" + indirim + " TL");
            yazici.println("TOPLAM TUTAR: " + toplam + " TL");
            yazici.println("-------------------------------------------");
            kucukGecikme(700);
            System.out.println("\n>>> Siparis ayrintilariyla 'siparis_gecmisi.txt' dosyasina kaydedildi.");
        } catch (IOException e) {
            System.out.println("Hata: Dosya yazilamadi!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        double[] yemekStoklari = {10.0, 8.0, 12.0, 5.0};
        double[] icecekStoklari = {20.0, 15.0, 15.0, 30.0};
        String[] yemekler = {"Nohutlu Pilav", "Tavuklu Pilav", "Kuru Fasulye & Pilav", "Ciger & Pilav"};
        double[] yemekFiyatlari = {120.0, 100.0, 110.0, 150.0};
        String[] icecekler = {"Ayran", "Kola", "Soda", "Su"};
        double[] icecekFiyatlari = {30.0, 60.0, 20.0, 15.0};

        System.out.println("Pilove Ev Yemekleri Otomasyonuna Hos Geldiniz!");
        kucukGecikme(800);

        System.out.print("Ad Soyad: "); 
        String ad = scanner.nextLine();
        System.out.print("Telefon: ");
        String tel = scanner.nextLine();
        System.out.print("Adres: ");
        String adres = scanner.nextLine();

        System.out.println("Fiyatlar 1 porsiyon / adet ücretidir...");
        menuGoster("Yemek Menumuz", yemekler, yemekFiyatlari);
        int ySecim = secimAl(scanner);
        double yMiktar = miktarAlVeStoktanDus(scanner, yemekStoklari, ySecim, "porsiyon");
        double yemekTutar = fiyatHesapla(yemekFiyatlari[ySecim - 1], yMiktar);

        menuGoster("Icecek Menumuz", icecekler, icecekFiyatlari);
        int iSecim = secimAl(scanner);
        double iMiktar = miktarAlVeStoktanDus(scanner, icecekStoklari, iSecim, "adet");
        double icecekTutar = fiyatHesapla(icecekFiyatlari[iSecim - 1], iMiktar);

        double araToplam = yemekTutar + icecekTutar;
        double indirimMiktari = 0;
        
        kucukGecikme(500);
        System.out.print("\nIndirim kodunuz var mi? (Yoksa H): ");
        String kupon = scanner.next();
        if (kupon.equalsIgnoreCase("PILOVE10")) {
            indirimMiktari = araToplam * 0.10;
            System.out.println("Kupon Kabul Edildi: -" + indirimMiktari + " TL indirim uygulandi!");
        }

        double sonFiyat = araToplam - indirimMiktari;

        kucukGecikme(500);
        System.out.println("\n--- SIPARIS FISI ---");
        System.out.println("Yemek: " + yemekler[ySecim-1] + " (" + yMiktar + " porsiyon) : " + yemekTutar + " TL");
        System.out.println("Icecek: " + icecekler[iSecim-1] + " (" + iMiktar + " adet) : " + icecekTutar + " TL");
        if (indirimMiktari > 0) System.out.println("PILOVE10 Indirimi: -" + indirimMiktari + " TL");
        System.out.println("-----------------------------------");
        System.out.println("TOPLAM ODENECEK: " + sonFiyat + " TL");
        
        dosyayaKaydet(ad, tel, adres, yemekler[ySecim-1], yMiktar, yemekTutar, icecekler[iSecim-1], iMiktar, icecekTutar, indirimMiktari, sonFiyat);
        
        System.out.println("\nBizi tercih ettiginiz icin tesekkurler, afiyet olsun!");
    }
}