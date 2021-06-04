# Lost and Found IPB

## Nama sistem, Paralel, Kelompok, Nama Asisten Praktikum
Nama Sistem : Lost and Found IPB <br>
Paralel : P2 <br>
Kelompok : 15 <br>
Nama Asisten Praktikum : 
1. G64180019 Levina Siatono
2. G64180080 Ali Naufal Ammarullah
3. G64180117 M. Fauzan Ramadhan

## Anggota Kelompok 15
1. G14190047 Alvin Christian : UI Designer & Android Developer
2. G64190064 Nabil Abidi : Android Developer & UI Designer
3. G64190083 Muhammad Reyhan : Backend Developer & Android Developer

## Deskripsi Singkat aplikasi
Lost and Found IPB merupakan sebuah aplikasi berbasis mobile yang berguna untuk mempertemukan orang-orang yang mengalami kehilangan barang dan orang-orang yang menemukan barang-barang tercecer yang terjadi di sekitar wilayah kampus IPB. Aplikasi ini akan memiliki fitur untuk memposting barang-barang yang hilang, memposting barang-barang tercecer yang ditemukan, memberikan komentar terhadap postingan tersebut, dan informasi dalam bentuk koordinat tempat barang itu ditemukan.

## User Analysis
Target dari aplikasi ini adalah anggota civitas IPB yang melakukan banyak aktivitas sehari-hari di lingkungan kampus IPB. Banyaknya aktivitas yang dilakukan terkadang membuat kita mengalami kehilangan barang atau bahkan menemukan barang milik orang lain. Aplikasi ini bertujuan untuk mempertemukan kedua pihak sehingga para civitas IPB dapat saling membantu satu sama lain dalam mencari barang yang terhilang.

### User Story
- Sebagai pengguna yang terdaftar, agar dapat menemukan barang yang hilang, saya dapat mem-posting kehilangan barang.
- Sebagai pengguna yang terdaftar, ketika saya menemukan barang seseorang, saya dapat melaporkan penemuan barang.
- Sebagai pengguna yang telah log out, agar dapat menggunakan aplikasi, saya perlu melakukan login dengan memasukkan username dan password.


## Spesifikasi Teknis Lingkungan Pengembangan
### Perangkat Keras (Hardware):  
Prosesor	: AMD A8 7410 APU  
Memori	: 8 GB DDR3  
Penyimpanan	: 120GB SSD dan 500GB HDD  
Kartu Grafis	: Radeon R5  

### Perangkat Lunak (Software):  
Sistem Operasi : Ubuntu LTS 20 dan Windows 10  

### Tech Stack:
Mobile Front-End  
IDE		 : Android Studio  
Back-End  
Text Editor	: Visual Studio Code  
Database	: PostgreSql  
Server		: Apache (Development)  
Framework	: Laravel  

## Hasil dan Pembahasan

### Use Case Diagram
![ERD   Usecase Lost and Found  IPB-Usecase](https://user-images.githubusercontent.com/70255413/119362585-0f8fb980-bcd7-11eb-9d64-8ac92a7bb177.png)

### Activity Diagram
![Activity Diagram Lost and Found IPB](https://user-images.githubusercontent.com/70255413/119362738-38b04a00-bcd7-11eb-85e2-3c1be514bcb9.png)

### Class Diagram
![Class Diagram Lost and Found IPB](https://user-images.githubusercontent.com/70255413/120824350-91f36580-c582-11eb-9f59-01546f253a34.png)

### Entity Relationship Diagram
![ERD   Usecase Lost and Found  IPB-ERD](https://user-images.githubusercontent.com/70255413/119362506-f8e96280-bcd6-11eb-9d16-91341b1b2269.png)

### System Architecture
![AD](https://user-images.githubusercontent.com/70255413/120831952-4e9cf500-c58a-11eb-83a1-a6e41772c84d.jpeg)

### Fungsi Utama yang Dikembangkan

### Fungsi CRUD

## Hasil Implementasi Perangkat Lunak
### Screenshot Sistem
Login Page
![login](https://user-images.githubusercontent.com/70255413/120833209-b869ce80-c58b-11eb-898e-70b35092566c.jpg)

Register Page
![regist](https://user-images.githubusercontent.com/70255413/120833254-c4559080-c58b-11eb-9681-29b429a75090.jpg)

Home
![home](https://user-images.githubusercontent.com/70255413/120833321-d59e9d00-c58b-11eb-8353-58a47bf949eb.png)

Formulir Pembuatan Post
![form](https://user-images.githubusercontent.com/70255413/120833341-dd5e4180-c58b-11eb-9a4d-bd2a7048e2f3.jpg)

Contoh Halaman Postingan Barang
![post](https://user-images.githubusercontent.com/70255413/120833353-e3542280-c58b-11eb-8ea1-b0246bacc928.png)

Profile Page
![profile](https://user-images.githubusercontent.com/70255413/120833368-e8b16d00-c58b-11eb-9ffb-faed34ae0287.jpg)

### Link Aplikasi
https://lostandfoundipb.herokuapp.com/

## Testing (Test Cases)
### Positive Cases
### Negative Cases

## Saran untuk Pengembangan Selanjutnya
- Jika Mendeploy Backend Service ke heroku, ada baiknya menyiapkan Amazon S3 untuk penyimpanan file atau bisa mengubah penyimpanan file ke public/database pada laravel (namun hal ini akan membebani storage pada heroku)
- Dapat menambahkan fitur untuk mengidentifikasi kepemilikan barang pada aplikasi. Hal ini akan membuat lokasi barang menjadi lebih terkontrol.
- Picture/foto/gambar untuk setiap postingan barang dapat dibuat lebih dari satu agar dapat memberikan gambaran yang lebih komprehensif dan membantu user dalam proses identifikasi barang
- Dapat disambungkan dengan SSO IPB sehingga hanya civitas IPB yang dapat mengakses aplikasi 
- Dapat ditambahkan fitur notifikasi sehingga jika ada post baru yang sesuai dengan barang yang dicari (memiliki ciri-ciri tertentu), user dapat menerima pemberitahuan
