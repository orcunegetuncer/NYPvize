package org.example;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int secim = 0;
        while (secim != 4) {

            System.out.println("Lütfen bir seçim yapın:");
            System.out.println("1- Elit üye ekleme");
            System.out.println("2- Genel üye ekleme");
            System.out.println("3- Mail Gönderme");
            System.out.println("4- Çıkış");

            secim = input.nextInt();

            switch (secim) {
                case 1:
                    Uye elitUser = new Uye();
                    System.out.print("Lütfen elit üyenin adını girin: ");
                    String elitUye = input.next();
                    elitUser.setName(elitUye);

                    System.out.print("Lütfen elit üyenin soyadını girin: ");
                    String elitUyeSoyismi = input.next();
                    elitUser.setSurname(elitUyeSoyismi);

                    System.out.print("Lütfen elit üyenin mail adresini girin: ");
                    String elitUyeMail = input.next();
                    elitUser.setEmail(elitUyeMail);

                    DosyaIslemleri.uyeEkle(UyeTip.ELIT, elitUser);
                    System.out.println(elitUye + " elit üye olarak eklendi.\n");
                    break;
                case 2:
                    Uye genelUser = new Uye();

                    System.out.print("Lütfen genel üyenin adını girin: ");
                    String genelUye = input.next();
                    genelUser.setName(genelUye);

                    System.out.print("Lütfen genel üyenin soyadını girin: ");
                    String genelUyeSoyismi = input.next();
                    genelUser.setSurname(genelUyeSoyismi);

                    System.out.print("Lütfen genel üyenin mail adresini girin: ");
                    String genelUyeMail = input.next();
                    genelUser.setEmail(genelUyeMail);

                    DosyaIslemleri.uyeEkle(UyeTip.GENEL, genelUser);
                    System.out.println(genelUye + " genel üye olarak eklendi.\n");
                    break;
                case 3:
                    int mailSecim = 0;


                    while (mailSecim != 4) {
                        System.out.println("Lütfen bir seçim yapın:");
                        System.out.println("1- Elit üyelere mail");
                        System.out.println("2- Genel üyelere mail");
                        System.out.println("3- Tüm üyelere mail");
                        System.out.println("4- Geri");

                        mailSecim = input.nextInt();

                        String baslik;
                        String mesaj;
                        switch (mailSecim) {
                            case 1:
                                System.out.print("Lütfen mailin başlığını giriniz: ");
                                baslik = input.next();
                                System.out.print("Lütfen mailin içeriğini giriniz: ");
                                mesaj = input.next();

                                EmailSender.mailGonder(baslik, mesaj, UyeTip.ELIT);
                                break;
                            case 2:
                                System.out.print("Lütfen mailin başlığını giriniz: ");
                                baslik = input.next();
                                System.out.print("Lütfen mailin içeriğini giriniz: ");
                                mesaj = input.next();

                                EmailSender.mailGonder(baslik, mesaj, UyeTip.GENEL);
                                break;
                            case 3:
                                System.out.print("Lütfen mailin başlığını giriniz: ");
                                baslik = input.next();
                                System.out.print("Lütfen mailin içeriğini giriniz: ");
                                mesaj = input.next();

                                EmailSender.mailGonder(baslik, mesaj, UyeTip.ELIT, UyeTip.GENEL);
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

class DosyaIslemleri {
    private static final String elitUyeBaslik = "#ELIT ÜYELER";
    private static final String genelUyeBaslik = "#GENEL ÜYELER";
    private static final String dosyaAdi = "uyeler.txt";

    public static List<String> mailListesi(UyeTip uyeTip) {
        // halihazırda olan üyeleri okuyup değişkene attım.
        Map<String, ArrayList<Uye>> uyeler = dosyaOku();
        // mail adreslerini döneceğim değişkeni oluşturdum.
        ArrayList<String> mailListesi = new ArrayList<>();
        // sadece elit ve genel üyelere mail atmak için ayrı değişkenler oluşturdum
        ArrayList<Uye> elitUyeList = uyeler.get(elitUyeBaslik);
        ArrayList<Uye> genelUyeList = uyeler.get(genelUyeBaslik);

        // elit üyelerin email adreslerinin değişkene atılması
        if (uyeTip.equals(UyeTip.ELIT)) {
            for (Uye uye : elitUyeList) {
                mailListesi.add(uye.getEmail());
            }
        }
        // genel üyelerin email adreslerinin değişkene atılması
        if (uyeTip.equals(UyeTip.GENEL)) {
            for (Uye uye : genelUyeList) {
                mailListesi.add(uye.getEmail());
            }
        }

        return mailListesi;
    }

    public static void uyeEkle(UyeTip uyeTip, Uye uye) {
        // önceden kaydedilmiş üyeleri dosyadan okuyup değişkene attım. yeni üyeleri altına eklemek için gerekliydi.
        Map<String, ArrayList<Uye>> uyeler = dosyaOku();
        // elit üyeler ile genel üyeleri farklı değişkenlere alıyorum. gelen isteğe göre altına ekleyeceğim.
        ArrayList<Uye> elitUyeList = uyeler.get(elitUyeBaslik);
        ArrayList<Uye> genelUyeList = uyeler.get(genelUyeBaslik);

        //eklenen üye elit ise ilgili listeye ekliyorum
        if (uyeTip.equals(UyeTip.ELIT)) {
            elitUyeList.add(uye);
        }

        //eklenen üye genel ise ilgili listeye ekliyorum
        if (uyeTip.equals(UyeTip.GENEL)) {
            genelUyeList.add(uye);
        }


        try {
            FileWriter dosya = new FileWriter(dosyaAdi);
            dosya.write(elitUyeBaslik + "\n"); // elit üye başlığını dosyaya yazdırdım
            for (Uye elitUye : elitUyeList) {
                // elit üyelerin hepsini alt alta aralarında tab olacak şekilde yazdırdım
                dosya.write(elitUye.getName() + "\t" + elitUye.getSurname() + "\t" + elitUye.getEmail() + "\n");
            }
            dosya.write("\n" + genelUyeBaslik + "\n"); // genel üye başlığını dosyaya yazdırdım
            for (Uye genelUye : genelUyeList) {
                // genel üyelerin hepsini alt alta aralarında tab olacak şekilde yazdırdım
                dosya.write(genelUye.getName() + "\t" + genelUye.getSurname() + "\t" + genelUye.getEmail() + "\n");
            }
            dosya.close();
        } catch (IOException e) {
            //herhangi bir hata olması durumunda hata mesajını ekrana yazdırdım.
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    private static Map<String, ArrayList<Uye>> dosyaOku() {
        // elit üyeler için list değişken tanımladım
        ArrayList<Uye> elitUyeler = new ArrayList<>();
        // genel üyeler için list değişken tanımladım
        ArrayList<Uye> genelUyeler = new ArrayList<>();
        // dosya okurken okunan kategoriyi bir değişkende tutuyorum. bunun sebebi elit ile genel üyeleri ayırmak.
        String mevcutKategori = "";

        try {
            BufferedReader okuyucu = new BufferedReader(new FileReader(dosyaAdi)); // yazdığım dosyayı okuyorum.
            String satir = okuyucu.readLine();
            while (satir != null) {
                // burda ilgili satır kategori ise mevcut kategoriyi değiştiriyorum.
                if (satir.contains(elitUyeBaslik)) {
                    mevcutKategori = elitUyeBaslik;
                }
                if (satir.contains(genelUyeBaslik)) {
                    mevcutKategori = genelUyeBaslik;
                }

                if (!satir.startsWith("#") && !satir.isBlank()) {
                    // aralarında tab olan verileri ayırıyorum
                    String[] parts = satir.split("\\t+");
                    String name = parts[0];
                    String surname = parts[1];
                    String email = parts[2];
                    Uye uye = new Uye(name, surname, email);
                    // kategoriye göre veriyi listeye ekliyorum.
                    if (mevcutKategori.equals(elitUyeBaslik)) {
                        elitUyeler.add(uye);
                    } else {
                        genelUyeler.add(uye);
                    }
                }

                satir = okuyucu.readLine();
            }
            okuyucu.close();
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
        // başlıklara göre map'in içerisine ekliyorum.
        return new HashMap<String, ArrayList<Uye>>() {{
            put(elitUyeBaslik, elitUyeler);
            put(genelUyeBaslik, genelUyeler);
        }};
    }
}

class Uye {
    private String name = "";
    private String surname = "";
    private String email = "";

    public Uye(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Uye() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

enum UyeTip {
    ELIT,GENEL;
}

class EmailSender {

    private static final String gondericiMailAdresi = "email adresinizi buraya girin";
    private static final String gondericiMailSifresi = "email şifrenizi buraya girin";

    public static void mailGonder(String konu, String icerik, UyeTip... uyeTip) {
        try {
            // verilen üye tiplerine göre mail adresi listesini oluşturdum
            List<String> mailListesi = new ArrayList<>();
            for (UyeTip tip : uyeTip) {
                List<String> tipeGoreMailListesi = DosyaIslemleri.mailListesi(tip);
                mailListesi.addAll(tipeGoreMailListesi);
            }

            // smtp mail ayarlarını yaptım
            Properties ayarlar = new Properties();
            ayarlar.put("mail.smtp.host", "smtp.gmail.com");
            ayarlar.put("mail.smtp.port", "587");
            ayarlar.put("mail.smtp.starttls.enable", "true");
            ayarlar.put("mail.smtp.auth", "true");

            // email ve şifre ile göndermek için oturum açtım.
            Session oturum = Session.getInstance(ayarlar, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(gondericiMailAdresi, gondericiMailSifresi);
                }
            });

            // mailin konusunu ve içeriğini setledim.
            Message mesaj = new MimeMessage(oturum);
            mesaj.setFrom(new InternetAddress(gondericiMailAdresi));
            mesaj.setSubject(konu);
            mesaj.setText(icerik);

            // tüm mail listesini for döngüsü kullanarak tek tek her mail adresine mail gönderdim.
            for (String email : mailListesi) {
                mesaj.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
                Transport.send(mesaj);
            }

        } catch (MessagingException e) {
            // herhangi bir hata durumunda hatayı yazmasını sağladım böylece karşılaştığım hatalara daha çabuk çözüm buldum.
            System.out.println("Hata:" + e.getMessage());
        }

    }

}