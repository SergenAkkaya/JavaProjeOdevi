import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class Pilove {

    private static int siparisSayaci = 1;
    private static final String DOSYA_YOLU = "siparis_gecmisi.txt";
    private static final String SAYAC_DOSYASI = "sayac.txt";

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
            System.out.print("Seciminiz: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Gecersiz! Bir rakam giriniz.");
                scanner.next();
            }
            secim = scanner.nextInt();
        } while (secim < 1 || secim > 5);
        return secim;
    }

    public static double miktarAlVeStoktanDus(Scanner scanner, double[] stoklar, int secim, String birim) {
        double miktar;
        do {
            System.out.print("Kac " + birim + " istersiniz? : ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Gecersiz miktar!");
                scanner.next();
            }
            miktar = scanner.nextDouble();
            if (miktar > stoklar[secim - 1]) {
                System.out.println("Hata: Stok yetersiz! Mevcut stok: " + stoklar[secim - 1]);
            }
        } while (miktar > stoklar[secim - 1]);

        stoklar[secim - 1] -= miktar;
        return miktar;
    }

    public static void sayaciYukle() {
        File dosya = new File(SAYAC_DOSYASI);
        if (dosya.exists()) {
            try (Scanner dosyaOkuyucu = new Scanner(dosya)) {
                if (dosyaOkuyucu.hasNextInt()) {
                    siparisSayaci = dosyaOkuyucu.nextInt();
                }
            } catch (Exception e) {}
        }
    }

    public static void sayaciKaydet() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAYAC_DOSYASI))) {
            pw.print(siparisSayaci);
        } catch (IOException e) {}
    }

    public static void dosyayaKaydet(int sNo, String ad, String tel, String adres, String urun, double uPors, double uTutar, String icecek, double iPors, double iTutar, double indirim, double toplam) {
        try (PrintWriter yazici = new PrintWriter(new FileWriter(DOSYA_YOLU, true))) {
            yazici.println(sNo + ";" + ad + ";" + tel + ";" + adres + ";" + urun + ";" + uPors + ";" + uTutar + ";" + icecek + ";" + iPors + ";" + iTutar + ";" + indirim + ";" + toplam);
            kucukGecikme(700);
            System.out.println("\n>>> Siparis No " + sNo + " sisteme kaydedildi.");
        } catch (IOException e) {
            System.out.println("Hata: Dosya yazilamadi!");
        }
    }

    public static void siparisYonetimi(Scanner scanner) {
        System.out.print("\nSorgulanacak Siparis No girin: ");
        int arananNo = scanner.nextInt();
        String[] tumSiparisler = new String[1000];
        int satirSayisi = 0;
        int bulunanIndex = -1;

        File dosya = new File(DOSYA_YOLU);
        if (!dosya.exists()) {
            System.out.println("Henuz hic siparis yok.");
            return;
        }

        try (Scanner dosyaOkuyucu = new Scanner(dosya)) {
            while (dosyaOkuyucu.hasNextLine() && satirSayisi < 1000) {
                String satir = dosyaOkuyucu.nextLine();
                tumSiparisler[satirSayisi] = satir;
                if (satir.startsWith(arananNo + ";")) {
                    bulunanIndex = satirSayisi;
                }
                satirSayisi++;
            }
        } catch (Exception e) {}

        if (bulunanIndex == -1) {
            System.out.println("Siparis bulunamadi!");
            return;
        }

        String[] d = tumSiparisler[bulunanIndex].split(";");
        System.out.println("\n--- SIPARIS DETAYLARI ---");
        System.out.println("Siparis No : " + d[0]);
        System.out.println("Musteri    : " + d[1]);
        System.out.println("Telefon    : " + d[2]);
        System.out.println("Adres      : " + d[3]);
        System.out.println("Yemek      : " + d[4] + " (" + d[5] + " porsiyon)");
        System.out.println("Yemek Tutar: " + d[6] + " TL");
        System.out.println("Icecek     : " + d[7] + " (" + d[8] + " adet)");
        System.out.println("Icecek Tut.: " + d[9] + " TL");
        System.out.println("Indirim    : " + d[10] + " TL");
        System.out.println("TOPLAM     : " + d[11] + " TL");
        System.out.println("-------------------------");

        System.out.println("1 )- Bilgileri Guncelle (Tel/Adres)");
        System.out.println("2 )- Siparişi Sil");
        System.out.println("3 )- Cikis");
        System.out.print("Seciminiz: ");
        int islem = scanner.nextInt();

        if (islem == 1) {
            scanner.nextLine();
            System.out.print("Yeni Telefon: "); d[2] = scanner.nextLine();
            System.out.print("Yeni Adres: "); d[3] = scanner.nextLine();
            tumSiparisler[bulunanIndex] = String.join(";", d);
            dosyayiGuncelle(tumSiparisler, satirSayisi);
            System.out.println("Guncelleme basarili.");
        } else if (islem == 2) {
            for (int i = bulunanIndex; i < satirSayisi - 1; i++) {
                tumSiparisler[i] = tumSiparisler[i + 1];
            }
            dosyayiGuncelle(tumSiparisler, satirSayisi - 1);
            System.out.println("Siparis silindi.");
        }
    }

    public static void dosyayiGuncelle(String[] veriler, int limit) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DOSYA_YOLU))) {
            for (int i = 0; i < limit; i++) {
                pw.println(veriler[i]);
            }
        } catch (IOException e) {}
    }

    public static void adminModulu(Scanner scanner, double[] yStok, double[] iStok, String[] yAd, String[] iAd) {
        System.out.print("Admin Sifresi: ");
        if (!scanner.next().equals("1234")) {
            System.out.println("Hatali Sifre! Ana menuye donuluyor.");
            return;
        }

        boolean adminPanel = true;
        while (adminPanel) {
            System.out.println("\n--- ADMIN YONETIM PANELI ---");
            System.out.println("1 )- Stoklari Goruntule");
            System.out.println("2 )- Stok Ekle (Guncelle)");
            System.out.println("3 )- Ana Menuye Don");
            int aSec = scanner.nextInt();

            if (aSec == 1) {
                System.out.println("\n--- GUNCEL STOK DURUMU ---");
                for (int i = 0; i < yAd.length; i++) System.out.println(yAd[i] + ": " + yStok[i]);
                for (int i = 0; i < iAd.length - 1; i++) System.out.println(iAd[i] + ": " + iStok[i]);
            } else if (aSec == 2) {
                System.out.print("Yemek (1) / Icecek (2): ");
                int t = scanner.nextInt();
                System.out.print("Urun Sira No: ");
                int s = scanner.nextInt() - 1;
                System.out.print("Eklenecek Miktar: ");
                double m = scanner.nextDouble();
                if (t == 1) yStok[s] += m; else iStok[s] += m;
                System.out.println("Stok guncellendi.");
            } else {
                adminPanel = false;
            }
        }
    }

    public static void main(String[] args) {
        sayaciYukle();
        Scanner scanner = new Scanner(System.in);

        double[] yemekStoklari = {10.0, 8.0, 12.0, 5.0};
        double[] icecekStoklari = {20.0, 15.0, 15.0, 30.0, 999.0};
        String[] yemekler = {"Nohutlu Pilav", "Tavuklu Pilav", "Kuru Fasulye & Pilav", "Ciger & Pilav"};
        double[] yemekFiyatlari = {100.0, 130.0, 110.0, 150.0};
        String[] icecekler = {"Ayran", "Kola", "Soda", "Su", "Istemiyorum"};
        double[] icecekFiyatlari = {30.0, 60.0, 20.0, 15.0, 0.0};

        while (true) {
            System.out.println("\n===============================");
            System.out.println("   PILOVE OTOMASYON SISTEMI");
            System.out.println("===============================");
            System.out.println("1 )- Kullanici Girisi");
            System.out.println("2 )- Admin Girisi");
            System.out.println("3 )- Sistemi Kapat");
            System.out.print("Seciminiz: ");
            int anaSec = scanner.nextInt();

            if (anaSec == 1) {
                System.out.println("\n--- KULLANICI ISLEMLERI ---");
                System.out.println("1 )- Yeni Siparis Ver");
                System.out.println("2 )- Siparis Sorgula / Guncelle / Sil");
                int kSec = scanner.nextInt();

                if (kSec == 1) {
                    scanner.nextLine();
                    System.out.print("Ad Soyad: "); String ad = scanner.nextLine();
                    System.out.print("Telefon: "); String tel = scanner.nextLine();
                    System.out.print("Adres: "); String adres = scanner.nextLine();

                    menuGoster("Yemek Menumuz", yemekler, yemekFiyatlari);
                    int ySec = secimAl(scanner);
                    double yMiktar = miktarAlVeStoktanDus(scanner, yemekStoklari, ySec, "porsiyon");
                    double yemekTutar = fiyatHesapla(yemekFiyatlari[ySec - 1], yMiktar);

                    menuGoster("Icecek Menumuz", icecekler, icecekFiyatlari);
                    int iSec = secimAl(scanner);
                    double iMiktar = 0;
                    double icecekTutar = 0;
                    if (iSec != 5) {
                        iMiktar = miktarAlVeStoktanDus(scanner, icecekStoklari, iSec, "adet");
                        icecekTutar = fiyatHesapla(icecekFiyatlari[iSec - 1], iMiktar);
                    }

                    double araToplam = yemekTutar + icecekTutar;
                    double indirimMiktari = 0;
                    System.out.print("\nVarsa Indirim Kodunuzu Girin (Yoksa H): ");
                    String kupon = scanner.next();
                    if (kupon.equalsIgnoreCase("PILOVE10")) {
                        indirimMiktari = araToplam * 0.10;
                        System.out.println("Tebrikler! %10 indirim uygulandi.");
                    }
                    double sonFiyat = araToplam - indirimMiktari;

                    System.out.println("\n--- SIPARISINIZ ALINDI ---");
                    System.out.println("Musteri: " + ad);
                    System.out.println("Yemek  : " + yemekler[ySec-1] + " x " + yMiktar + " Pors. = " + yemekTutar + " TL");
                    if (iSec != 5) {
                        System.out.println("Icecek : " + icecekler[iSec-1] + " x " + iMiktar + " Adet = " + icecekTutar + " TL");
                    }
                    if (indirimMiktari > 0) {
                        System.out.println("Indirim: -" + indirimMiktari + " TL");
                    }
                    System.out.println("--------------------------");
                    System.out.println("TOPLAM ODENECEK: " + sonFiyat + " TL");
                    System.out.println("--------------------------");

                    dosyayaKaydet(siparisSayaci, ad, tel, adres, yemekler[ySec - 1], yMiktar, yemekTutar, icecekler[iSec - 1], iMiktar, icecekTutar, indirimMiktari, sonFiyat);
                    siparisSayaci++;
                    sayaciKaydet();
                } else {
                    siparisYonetimi(scanner);
                }
            } else if (anaSec == 2) {
                adminModulu(scanner, yemekStoklari, icecekStoklari, yemekler, icecekler);
            } else if (anaSec == 3) {
                sayaciKaydet();
                System.out.println("Sistemden cikis yapildi. Afiyet olsun!");
                break;
            }
        }
        scanner.close();
    }
}