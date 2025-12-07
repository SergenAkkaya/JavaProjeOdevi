import java.util.Scanner;

public class Pilove {

    public static double FiyatHesapla(double UrunFiyat, double UrunPorsiyon) {
        double toplamFiyat = UrunFiyat * UrunPorsiyon;
        return toplamFiyat;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pilove Ev Yemekleri Sipariş Sistemine Hoş Geldiniz!");
        kucukGecikme(700);
        System.out.println("\n--- Lütfen Bilgilerinizi Giriniz ---");
        kucukGecikme(300);

        System.out.print("Adınız ve Soyadınız: ");
        String adSoyad = scanner.nextLine();
        kucukGecikme(100);

        System.out.print("Telefon Numaranız: ");
        String telNo = scanner.nextLine();
        kucukGecikme(100);

        System.out.print("Teslimat Adresiniz: ");
        String adres = scanner.nextLine();
        kucukGecikme(100);

        System.out.println("\n Bilgileriniz başarıyla kaydedildi.");
        kucukGecikme(500);

        System.out.println("--- Menüye Geçiliyor... ---");
        kucukGecikme(500);

        System.out.println("Dikkat!");
        System.out.println("Siparişler içeriklerin numaralarına göre alınır.");

        double toplam = 0;
        int KullaniciSecimi = 0;
        String KullaniciUrunAd = "";
        double KullaniciUrunFiyat =0;
        double KullaniciUrunPorsiyon =0;

        String KullaniciIcecekAd = "";
        double KullaniciIcecekFiyat =0;
        double KullaniciIcecekPorsiyon =0;
        System.out.println("--- Yemek Menümüz ---");
        System.out.println(" 1 )- Nohutlu Pilav");
        System.out.println(" 2 )- Tavuklu Pilav");
        System.out.println(" 3 )- Kuru Fasulye & Pilav");
        System.out.println(" 4 )- Ciğer & Pilav");
        System.out.print("Seçiminiz : ");
        KullaniciSecimi = scanner.nextInt();
        if (KullaniciSecimi > 0 && KullaniciSecimi <= 4) {
            switch (KullaniciSecimi) {
                case 1:
                    KullaniciUrunAd = "Nohutlu Pilav";
                    System.out.print("Porsiyon Boyutunu Giriniz : ");
                    KullaniciUrunPorsiyon = scanner.nextDouble();
                    KullaniciUrunFiyat = FiyatHesapla(120, KullaniciUrunPorsiyon);
                    break;
                case 2:
                    KullaniciUrunAd = "Tavuklu Pilav";
                    System.out.print("Porsiyon Boyutunu Giriniz : ");
                    KullaniciUrunPorsiyon = scanner.nextDouble();
                    KullaniciUrunFiyat = FiyatHesapla(100, KullaniciUrunPorsiyon);
                    break;
                case 3:
                    KullaniciUrunAd = "Kuru Fasulye & Pilav";
                    System.out.print("Porsiyon Boyutunu Giriniz : ");
                    KullaniciUrunPorsiyon = scanner.nextDouble();
                    KullaniciUrunFiyat = FiyatHesapla(110, KullaniciUrunPorsiyon);
                    break;
                case 4:
                    KullaniciUrunAd = "Ciğer & Pilav";
                    System.out.print("Porsiyon Boyutunu Giriniz : ");
                    KullaniciUrunPorsiyon = scanner.nextDouble();
                    KullaniciUrunFiyat = FiyatHesapla(150, KullaniciUrunPorsiyon);
                    break;
            }
            toplam += KullaniciUrunFiyat;
        kucukGecikme(500);

        }
        else{
            System.out.println("Hatalı Giriş!");
            KullaniciSecimi = scanner.nextInt(); 
        }

        System.out.println("--- İçecek Menümüz ---");
        System.out.println(" 1 )- Ayran");
        System.out.println(" 2 )- Kola");
        System.out.println(" 3 )- Soda");
        System.out.println(" 4 )- Su");
        System.out.print("Seçiminiz : ");
        KullaniciSecimi = scanner.nextInt();
        if (KullaniciSecimi > 0 && KullaniciSecimi <= 4) {
            switch (KullaniciSecimi) {
                case 1:
                    KullaniciIcecekAd = "Ayran";
                    System.out.print("İçecek Adedini Giriniz : ");
                    KullaniciIcecekPorsiyon = scanner.nextDouble();
                    KullaniciIcecekFiyat = FiyatHesapla(30, KullaniciIcecekPorsiyon);
                    break;
                case 2:
                    KullaniciIcecekAd = "Kola";
                    System.out.print("İçecek Adedini Giriniz : ");
                    KullaniciIcecekPorsiyon = scanner.nextDouble();
                    KullaniciIcecekFiyat = FiyatHesapla(60, KullaniciIcecekPorsiyon);
                    break;
                case 3:
                    KullaniciIcecekAd = "Soda";
                    System.out.print("İçecek Adedini Giriniz : ");
                    KullaniciIcecekPorsiyon = scanner.nextDouble();
                    KullaniciIcecekFiyat = FiyatHesapla(20, KullaniciIcecekPorsiyon);
                    break;
                case 4:
                    System.out.print("İçecek Adedini Giriniz : ");
                    KullaniciIcecekAd = "Su";
                    KullaniciIcecekPorsiyon = scanner.nextDouble();
                    KullaniciIcecekFiyat = FiyatHesapla(15, KullaniciIcecekPorsiyon);
                    break;
            }
            toplam += KullaniciIcecekFiyat;
        kucukGecikme(500);

        }
        else{
            System.out.println("Hatalı Giriş!");
            KullaniciSecimi = scanner.nextInt(); 
        }

        System.out.println("Siparişiniz Tamamlandı! Sipariş Özetiniz: ");
        kucukGecikme(500);

        System.out.println("İsim Soyisim : "+ adSoyad);
        System.out.println("Telefon Numarası :"+telNo + " // Adres : " + adres);
        System.out.println("Ürünler : " + KullaniciUrunAd + " // " + KullaniciUrunPorsiyon+ " // " + KullaniciUrunFiyat);
        System.out.println("İçecekler : " + KullaniciIcecekAd + " // " + KullaniciIcecekPorsiyon +" // "+ KullaniciIcecekFiyat);
        System.out.println("Toplam Fiyat : "+ toplam);
        System.out.println("Siparişiniz için Teşekkürler");
    }

    public static void kucukGecikme(long milisaniye) {
        try {
            Thread.sleep(milisaniye);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}