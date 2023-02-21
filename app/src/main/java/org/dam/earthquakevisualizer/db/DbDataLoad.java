package org.dam.earthquakevisualizer.db;

import org.dam.earthquakevisualizer.javabeans.Country;
import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class DbDataLoad {
    public static final ArrayList<Earthquake> EARTHQUAKES = new ArrayList<>(Arrays.asList(
            new Earthquake("22 de mayo de 1960, 15:11", 9.5, "Terremoto de Valdivia de 1960","Valdivia, Región de los Ríos", "38°14′24′′S 73°3′0′′O", "1655 a 2000"),
            new Earthquake("26 de diciembre de 2004, 07:58", 9.3, "Terremoto del océano Índico de 2004","Frente al norte de la isla de Sumatra", "Sin datos", "230270"),
            new Earthquake("27 de marzo de 1964, 17:36", 9.2, "Terremoto de Alaska de 1964","Anchorage, Alaska", "61°N 148°O", "128"),
            new Earthquake("11 de marzo de 2011, 14:46", 9.1, "Terremoto y maremoto de Japón de 2011","Costa Este de la Región de Tōhoku, Honshū", "38°19′19.20′′N 142°22′8.40′′E","15897"),
            new Earthquake("4 de noviembre de 1952, 16:58", 9.0, "Terremoto de Kamchatka de 1952","Península de Kamchatka", "52°48′N 159°30′E", "2366"),
            new Earthquake("13 de agosto de 1868, 21:30", 9.0, "Terremoto de Arica de 1868", "Arica","18°36′S 71°0′O", "693"),
            new Earthquake("28 de octubre de 1746, 22:30", 9.0, "Terremoto de Lima de 1746","Lima y Callao", "11°21′00′′S 77°16′48′′O", "15000 a 20000"),
            new Earthquake("26 de enero de 1700, 21:30", 9.0, "Terremoto de Cascadia de 1700","California, Oregón, Washington y Columbia Británica", "Sin datos", "Sin datos"),
            new Earthquake("27 de febrero de 2010, 03:34", 8.8, "Terremoto de Chile de 2010","Cobquecura, Región del Biobío (actual Ñuble)", "35°50′45.6′′S 72°42′57.6′′O","525"),
            new Earthquake("31 de enero de 1906, 15:36", 8.8, "Terremoto de Ecuador y Colombia de 1906", "Frente a las costas de Esmeraldas", "1°0′N 81°30′O", "1500"),
            new Earthquake("25 de noviembre de 1833, 20:00", 8.8, "Terremoto de Sumatra de 1833", "En el mar al sur de la isla de Sumatra, a 175 km al sur de Padang", "3°30′S 102°12′E", "Sin datos"),
            new Earthquake("1 de noviembre de 1755, 10:16", 8.7, "Terremoto de Lisboa de 1755", "Lisboa", "36°N 11°O", "60000 a 100000"),
            new Earthquake("8 de julio de 1730, 04:45", 8.7, "Terremoto de Valparaíso de 1730", "Valparaíso y La Serena", "33°30′S 71°36′O", "300"),
            new Earthquake("11 de abril de 2012, 15:38", 8.6, "Terremoto del océano Índico de 2012", "Frente al norte de la isla de Sumatra", "02°18′39.6′′N 93°03′46.8′′E", "10"),
            new Earthquake("28 de marzo de 2005, 23:09", 8.6, "Terremoto de Sumatra de 2005", "Frente al norte de la isla de Sumatra", "2°36′N 97°6′E", "1300"),
            new Earthquake("9 de marzo de 1957, 14:22", 8.6, "Terremoto de las islas Andreanof de 1957", "Islas Andreanof, Alaska", "51°33′36′′N 175°23′24′′O", "0"),
            new Earthquake("15 de agosto de 1950", 8.6, "Terremoto de Assam de 1950", "Assam (India), Tíbet (China)", "28°30′N 96°30′O", "1526"),
            new Earthquake("10 de noviembre de 1922, 23:53", 8.6, "Terremoto de Vallenar de 1922 Chile", "Región de Atacama y Provincia de Catamarca", "Sin datos", "1500"),
            new Earthquake("28 de marzo de 1787, 11:30", 8.6, "Terremoto de Nueva España de 1787", "Costas de Oaxaca y Guerrero", "Sin datos", "11"),
            new Earthquake("3 de febrero de 1923, 04:58", 8.5, "Terremoto de Kamchatka de 1923", "Península de Kamchatka", "54°N 161°E", "Sin datos"),
            new Earthquake("20 de octubre de 1687, 09:15", 8.5, "Terremotos de Lima y Callao de 1687", "Lima y Callao", "Sin datos", "5000"),
            new Earthquake("16 de diciembre de 1575, 14:30", 8.5, "Terremoto de Valdivia de 1575", "Valdivia", "39°48′S 73°12′O", "1221"),
            new Earthquake("16 de septiembre de 2015, 19:54", 8.4, "Terremoto de Coquimbo de 2015","Frente a las costas de la Comuna de Canela, Región de Coquimbo", "31°34′52′′S 71°45′07′′O", "12"),
            new Earthquake("23 de junio de 2001, 15:33", 8.4, "Terremoto del sur del Perú de 2001", "Departamentos de Arequipa, Moquegua y Tacna", "16°15′36′′S 73°38′24′′O","102"),
            new Earthquake("6 de febrero de 2023, 01:17", 7.8, "Terremoto de Turquía y Siria 2023","Turquía, Siria", "37°10′26′′N 37°01′55′′E", "40120")
    ));
    public static final ArrayList<Country> COUNTRIES = new ArrayList<>(Arrays.asList(
            new Country("22 de mayo de 1960, 15:11", "Chile"),
            new Country("26 de diciembre de 2004, 07:58", "Indonesia"),
            new Country("27 de marzo de 1964, 17:36", "Estados Unidos"),
            new Country("11 de marzo de 2011, 14:46", "Japón"),
            new Country("4 de noviembre de 1952, 16:58", "Rusia"),
            new Country("13 de agosto de 1868, 21:30", "Chile"),
            new Country("28 de octubre de 1746, 22:30", "Perú"),
            new Country("26 de enero de 1700, 21:30", "Estados Unidos"),
            new Country("27 de febrero de 2010, 03:34", "Chile"),
            new Country("6 de febrero de 2023, 01:17", "Turquía"),
            new Country("6 de febrero de 2023, 01:17", "Siria"),
            new Country("31 de enero de 1906, 15:36", "Ecuador"),
            new Country("31 de enero de 1906, 15:36", "Colombia"),
            new Country("25 de noviembre de 1833, 20:00", "Indonesia"),
            new Country("1 de noviembre de 1755, 10:16", "Portugal"),
            new Country("8 de julio de 1730, 04:45", "Chile"),
            new Country("11 de abril de 2012, 15:38", "Indonesia"),
            new Country("28 de marzo de 2005, 23:09", "Indonesia"),
            new Country("9 de marzo de 1957, 14:22", "Estados Unidos"),
            new Country("15 de agosto de 1950", "India"),
            new Country("15 de agosto de 1950", "China"),
            new Country("10 de noviembre de 1922, 23:53", "Argentina"),
            new Country("28 de marzo de 1787, 11:30", "México"),
            new Country("3 de febrero de 1923, 04:58", "Rusia"),
            new Country("20 de octubre de 1687, 09:15", "Perú"),
            new Country("16 de diciembre de 1575, 14:30", "Chile"),
            new Country("16 de septiembre de 2015, 19:54", "Chile"),
            new Country("23 de junio de 2001, 15:33", "Perú")
    ));
}
