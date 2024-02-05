# SR2

Tiimi: [Bozic Zorana](https://github.com/zokaas), [Lairi Piia](https://github.com/piialairi), [Martinonyte Dovile](https://github.com/dovile-mart), [Montonen Joonas](https://github.com/F0rsu), [Muittari Samuel](https://github.com/samuelmuittari), [Rautiainen Aleksis](https://github.com/aleraut), [Rusi Romeo](https://github.com/romeorusi).
<!--
## Johdanto

Johdantoon kirjoitetaan lyhyt, ytimekäs kuvaus siitä, mikä on projektin aihe,
kuka on asiakas (käyttäjä), mitä hän haluaa ja saa järjestelmältä, mitä
tekniikoita käytetään ja mitä konkreettisesti on valmiina, kun projekti päättyy.

-   Järjestelmän tarkoitus ja tiivis kuvaus siitä, mistä on kyse ja kenelle järjestelmä on tarkoitettu.
-   Toteutus- ja toimintaympäristö lyhyesti:  
    -   Palvelinpuolen ratkaisut ja teknologiat (esim. palvelinteknologia, mikä tietokantajärjestelmä on käytössä)
    -   Käyttöliittymäratkaisut ja teknologiat (esim. päätelaitteet: puhelin,
    täppäri, desktop)
    
Web-sovelluksen tarkoituksena on antaa yritykselle, kuljetusliikkeille ja käsittelylaitokselle yhteinen sovellus, jonka kautta voidaan hallinnoida, varata ja laskuttaa akku- ja paristokierrätyksen tarvittavia lavoja. Ennen tätä sovellusta eri toimijoilla on ollut käytössä vaihtelevia tapoja tehdä näitä toimintoja. Projektin tarkoituksena on tehdä asiakkaalle prototyyppi. 
Sovelluksen tekemiseen käytetään JHipsteriä -kehitystyökalu, joka tarjoaa mm. valmiin käyttäjänhallinnan, tietokantarajapinnat sekä tietoturvan.

Sovelluksessa on palvelinpuolella käytössä Spring Boot ja kehitysvaiheessa käytössä on H2-tietokanta. Käyttöliittymäratkaisuna on React TypeScriptillä ja sovellusta on tarkoitus käyttää pääasiallisesti tietokoneella, mutta sen skaalautuvuus on suunniteltu siten, että sitä voi käyttää myös mobiililaitteella. 

## Järjestelmän määrittely

Määrittelyssä järjestelmää tarkastellaan käyttäjän näkökulmasta. Järjestelmän
toiminnot hahmotellaan käyttötapausten tai käyttäjätarinoiden kautta, ja kuvataan järjestelmän
käyttäjäryhmät.

-   Lyhyt kuvaus käyttäjäryhmistä (rooleista)
Sovelluksen käyttäjät: Yritys, Käsittelylaitos, Kuljetusliikkeet 1-4
-   Käyttäjäroolit ja roolien tarvitsemat toiminnot, esim. käyttötapauskaaviona
    (use case diagram) tai käyttäjätarinoina.
-   Lyhyt kuvaus käyttötapauksista tai käyttäjätarinat

Kuvauksissa kannattaa harkita, mikä on toteuttajalle ja asiakkaalle oleellista
tietoa ja keskittyä siihen.

## Käyttöliittymä

Esitetään käyttöliittymän tärkeimmät (vain ne!) näkymät sekä niiden väliset siirtymät käyttöliittymäkaaviona. 

Jos näkymän tarkoitus ei ole itsestään selvä, se pitää kuvata lyhyesti.
-->

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
<!--
## Tietokanta

Järjestelmään säilöttävä ja siinä käsiteltävät tiedot ja niiden väliset suhteet
kuvataan käsitekaaviolla. Käsitemalliin sisältyy myös taulujen välisten viiteyhteyksien ja avainten
määritykset. Tietokanta kuvataan käyttäen jotain kuvausmenetelmää, joko ER-kaaviota ja UML-luokkakaaviota.

Lisäksi kukin järjestelmän tietoelementti ja sen attribuutit kuvataan
tietohakemistossa. Tietohakemisto tarkoittaa yksinkertaisesti vain jokaisen elementin (taulun) ja niiden
attribuuttien (kentät/sarakkeet) listausta ja lyhyttä kuvausta esim. tähän tyyliin:

> ### _Tilit_
> _Tilit-taulu sisältää käyttäjätilit. Käyttäjällä voi olla monta tiliä. Tili kuuluu aina vain yhdelle käyttäjälle._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> id | int PK | Tilin id
> nimimerkki | varchar(30) |  Tilin nimimerkki
> avatar | int FK | Tilin avatar, viittaus [avatar](#Avatar)-tauluun
> kayttaja | int FK | Viittaus käyttäjään [käyttäjä](#Kayttaja)-taulussa

## Tekninen kuvaus

Teknisessä kuvauksessa esitetään järjestelmän toteutuksen suunnittelussa tehdyt tekniset
ratkaisut, esim.

-   Missä mikäkin järjestelmän komponentti ajetaan (tietokone, palvelinohjelma)
    ja komponenttien väliset yhteydet (vaikkapa tähän tyyliin:
    https://security.ufl.edu/it-workers/risk-assessment/creating-an-information-systemdata-flow-diagram/)
-   Palvelintoteutuksen yleiskuvaus: teknologiat, deployment-ratkaisut yms.
-   Keskeisten rajapintojen kuvaukset, esimerkit REST-rajapinta. Tarvittaessa voidaan rajapinnan käyttöä täsmentää
    UML-sekvenssikaavioilla.
-   Toteutuksen yleisiä ratkaisuja, esim. turvallisuus.

Tämän lisäksi

-   ohjelmakoodin tulee olla kommentoitua
-   luokkien, metodien ja muuttujien tulee olla kuvaavasti nimettyjä ja noudattaa
    johdonmukaisia nimeämiskäytäntöjä
-   ohjelmiston pitää olla organisoitu komponentteihin niin, että turhalta toistolta
    vältytään

## Testaus

Tässä kohdin selvitetään, miten ohjelmiston oikea toiminta varmistetaan
testaamalla projektin aikana: millaisia testauksia tehdään ja missä vaiheessa.
Testauksen tarkemmat sisällöt ja testisuoritusten tulosten raportit kirjataan
erillisiin dokumentteihin.

Tänne kirjataan myös lopuksi järjestelmän tunnetut ongelmat, joita ei ole korjattu.

## Asennustiedot

Järjestelmän asennus on syytä dokumentoida kahdesta näkökulmasta:

-   järjestelmän kehitysympäristö: miten järjestelmän kehitysympäristön saisi
    rakennettua johonkin toiseen koneeseen

-   järjestelmän asentaminen tuotantoympäristöön: miten järjestelmän saisi
    asennettua johonkin uuteen ympäristöön.

Asennusohjeesta tulisi ainakin käydä ilmi, miten käytettävä tietokanta ja
käyttäjät tulee ohjelmistoa asentaessa määritellä (käytettävä tietokanta,
käyttäjätunnus, salasana, tietokannan luonti yms.).

## Käynnistys- ja käyttöohje

Tyypillisesti tässä riittää kertoa ohjelman käynnistykseen tarvittava URL sekä
mahdolliset kirjautumiseen tarvittavat tunnukset. Jos järjestelmän
käynnistämiseen tai käyttöön liittyy joitain muita toimenpiteitä tai toimintajärjestykseen liittyviä asioita, nekin kerrotaan tässä yhteydessä.

Usko tai älä, tulet tarvitsemaan tätä itsekin, kun tauon jälkeen palaat
järjestelmän pariin !


Back end sovellus käynnistyy komentorivillä komennolla "./mvnw" osoitteessa "localhost:8080" ja front end -sovellus käynnistyy komennolla "npm start" osoitteessa "localhost:"

Sovellus on käytettävissä myös osoitteessa: "tähän tulee heroku tms. osoite"
-->
