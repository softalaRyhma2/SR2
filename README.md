# SR2

#### Käyttäjäroolit 

_Yritys (Admin) voi:_

- nähdä **missä** varastossa lavat ovat (käsittelylaitos/kuljetusliike)
- nähdä **montako** lavaa jokaisessa varastossa on ja **kauanko** ne siellä säilytetään.
- muokata lavapaikkahintoja


_Käsittelylaitos voi:_

- nähdä montako tyhjiä/ varattuja lavoja omassa varastossa on
- muokata oman varaston saldoa
- nähdä ja muokata tyhjien lavapaikkojen määrät
- nähdä ja **muokata(?) oma** varastointihinta per lava **(muuttuuko kerran kuukaudessa vai useimmin)**
- laskuttaa yritystä kuukausittain varastossa säilytetyistä lavoista
- tulostaa yhteenvedon (varastosaldo+päivähinta) csv-muodossa.
- **Ei saa nähdä mitään tietoja kuljetusliikkeistä**



_Kuljetusliike voi:_

- nähdä montako lavaa omassa varastossa on
- muokata oman varaston saldoa
- nähdä montako **vapaata** lavaa käsittelylaitoksen varastossa on ja **varata** niitä
- nähdä **oma** varastointihinta per lava ** (muuttuuko kerran kuukaudessa vai useimmin)**
- laskuttaa yritystä kuukausittain varastossa säilytetyistä lavoista
- tulostaa yhteenvedon (varastosaldo+päivähinta) csv-muodossa.

#### Käyttäjätarinat

|Käyttäjä | Tekee | Miksi|
|---|---|---|
|Yritys |Haluaa kirjautua järjestelmään | Näkee käsittelylaitoksen ja kuljetusliikkeiden varastotietoja|
|Yritys |Haluaa nähdä varaston historiaa | Voi seurata paljonko keräysvälineitä on ollut ja missä |
|Käsittelylaitos |Haluaa nähdä yhteenvedon | Pystyy laskuttamaan yritystä|
|Käsittelylaitos| Haluaa kirjautua järjestelmään | Näkee ja pääsee muokkaamaan varastotietoja|
|Käsittelylaitos| Haluaa nähdä ja muokata lavasäilytyshinta | Pystyy laskuttamaan yritystä ajantasaisesti |
|Käsittelylaitos | Näkee omia varastotietoja | Pystyy laskuttamaan yritystä |
|Kuljetusliike | Haluaa kirjautua järjestelmään | Näkee varastotietoja | 
|Kuljetusliike | Näkee omia varastotietoja | Pystyy laskuttamaan yritystä |
|Kuljetusliike | Haluaa nähdä käsittelylaitoksen varastossa olevat vapaat lavat | Pysty varaamaan lavat | 
|Kuljetusliike | Haluaa varata tyhjiä lavoja | Voi kuljettaa **kokonaisen** kuorman omaan varastoon | 
|Kuljetusliike |Haluaa nähdä yhteenvedon | Pystyy laskuttamaan yritystä|
|Kuljetusliike | Haluaa nähdä ja muokata lavasäilytyshinta | Pystyy laskuttamaan yritystä ajantasaisesti |
| | | | 
