package com.epam.training.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

/*
* # Bejelentkezés adminisztrátorként
sign in privileged admin admin

# Movie létrehozása
create movie "Spirited Away" animation 125
create movie "Inception" thriller 148

# Room létrehozása
create room "Pedersoli" 10 15
create room "Nolan" 8 12

# Screening létrehozás: sikeres
create screening "Spirited Away" Pedersoli "2021-03-14 16:00"

# Screening létrehozás: érvénytelen dátumformátum
create screening "Spirited Away" Pedersoli "14-03-2021 16:00"

# Screening létrehozás: ütköző vetítés
create screening "Spirited Away" Pedersoli "2021-03-14 16:30"

# Screening létrehozás: szünet időszakában kezdődik
create screening "Spirited Away" Pedersoli "2021-03-14 17:00"

# Screening létrehozás: nem létező film
create screening "NonExistentMovie" Pedersoli "2021-03-14 18:00"

# Screening létrehozás: nem létező terem
create screening "Spirited Away" NonExistentRoom "2021-03-14 19:00"

delete screening "Spirited Away" Pedersoli "2021-03-14 16:00"

list screenings
* */
