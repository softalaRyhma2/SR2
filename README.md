# SR2

Tiimi: [Bozic Zorana](https://github.com/zokaas), [Lairi Piia](https://github.com/piialairi), [Martinonyte Dovile](https://github.com/dovile-mart), [Montonen Joonas](https://github.com/F0rsu), [Muittari Samuel](https://github.com/samuelmuittari), [Rautiainen Aleksis](https://github.com/aleraut), [Rusi Romeo](https://github.com/romeorusi).

## Johdanto
<!--
Johdantoon kirjoitetaan lyhyt, ytimekäs kuvaus siitä, mikä on projektin aihe,
kuka on asiakas (käyttäjä), mitä hän haluaa ja saa järjestelmältä, mitä
tekniikoita käytetään ja mitä konkreettisesti on valmiina, kun projekti päättyy.

-   Järjestelmän tarkoitus ja tiivis kuvaus siitä, mistä on kyse ja kenelle järjestelmä on tarkoitettu.
-   Toteutus- ja toimintaympäristö lyhyesti:  
    -   Palvelinpuolen ratkaisut ja teknologiat (esim. palvelinteknologia, mikä tietokantajärjestelmä on käytössä)
    -   Käyttöliittymäratkaisut ja teknologiat (esim. päätelaitteet: puhelin,
    täppäri, desktop)

    -->
    
Web-sovelluksen tarkoituksena on antaa yritykselle, kuljetusliikkeille ja käsittelylaitokselle yhteinen sovellus, jonka kautta voidaan hallinnoida, varata ja laskuttaa akku- ja paristokierrätykseen tarvittavia lavoja. Ennen tätä sovellusta eri toimijoilla on ollut käytössä vaihtelevia tapoja tehdä näitä toimintoja. Projektin tarkoituksena on tehdä asiakkaalle prototyyppi. 
Sovelluksen tekemiseen käytetään JHipsteriä -kehitystyökalua, joka tarjoaa mm. valmiin käyttäjänhallinnan, tietokantarajapinnat sekä tietoturvan.

Sovelluksessa on palvelinpuolella käytössä Spring Boot ja kehitysvaiheessa käytössä on H2-tietokanta. Käyttöliittymäratkaisuna on React TypeScriptillä ja sovellusta on tarkoitus käyttää pääasiallisesti tietokoneella, mutta sen skaalautuvuus on suunniteltu siten, että sitä voi käyttää myös mobiililaitteella. 
<!--
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
-->
## Käyttöliittymä
### Käyttöliitymän prototyyppi

Käyttöliittymän prototyypin toteutus [Figmalla](https://www.figma.com/proto/ro5H0JYtuOTCF1MvtBOJzP/Battery?type=design&node-id=2-3&scaling=min-zoom&page-id=0%3A1&starting-point-node-id=2%3A3).

<!--
Esitetään käyttöliittymän tärkeimmät (vain ne!) näkymät sekä niiden väliset siirtymät käyttöliittymäkaaviona. 

Jos näkymän tarkoitus ei ole itsestään selvä, se pitää kuvata lyhyesti.
-->

#### Käyttäjäroolit 

_Yritys (Admin) voi:_

- nähdä käsittelylaitoksen ja kuljetusliikkeen varastosaldot ja muokata niitä.
- muokata käsittelylaitoksen ja kuljetusliikkeen lavapaikkahintoja.
- nähdä käsittelylaitoksen ja kuljetusliikkeen laskuhistoriat.
- nähdä käsittelylaitoksen ja kuljetusliikkeen organisaatiotietoja.

_Käsittelylaitos voi:_

- nähdä oman varaston tyhjät ja varatut lavat.
- muokata oman varaston saldoa.
- nähdä oma varastointihinta per lava.
- laskuttaa yritystä kuukausittain varastossa säilytetyistä lavoista.
- tulostaa yhteenvedon (varastosaldo+päivähinta) csv-muodossa.
- **Ei saa nähdä mitään tietoja kuljetusliikkeistä**



_Kuljetusliike voi:_

- nähdä ja muokata oman varaston saldoa.
- nähdä montako varattavissa olevaa lavaa käsittelylaitoksen varastossa on, varata niitä ja **merkata noudetuksi**.
- nähdä oma varastointihinta per lava.
- laskuttaa yritystä kuukausittain varastossa säilytetyistä lavoista.
- tulostaa yhteenvedon (varastosaldo+päivähinta) csv-muodossa.

#### Käyttäjätarinat

|Käyttäjä | Tekee | Miksi|
|---|---|---|
|Yritys |Haluaa kirjautua järjestelmään | Näkee käsittelylaitoksen ja kuljetusliikkeiden varastotietoja|
|Yritys | Haluaa nähdä ja muokata lavasäilytyshinta | Tieto järjestelmässä on ajantasainen |
|Yritys |Haluaa nähdä varaston historiaa | Voi seurata paljonko keräysvälineitä on ollut ja missä |
|Yritys | Haluaa nähdä organisaatiotietoja | Pystyy ottamaan yhteyttä organisaatioon |
|Käsittelylaitos |Haluaa nähdä yhteenvedon | Pystyy laskuttamaan yritystä|
|Käsittelylaitos| Haluaa kirjautua järjestelmään | Näkee ja pääsee muokkaamaan varastotietoja|
|Käsittelylaitos| Haluaa nähdä lavasäilytyshinta | Pystyy laskuttamaan yritystä ajantasaisesti |
|Käsittelylaitos | Näkee omia varastotietoja | Pystyy laskuttamaan yritystä |
|Kuljetusliike | Haluaa kirjautua järjestelmään | Näkee varastotietoja | 
|Kuljetusliike | Näkee omia varastotietoja | Pystyy laskuttamaan yritystä |
|Kuljetusliike | Haluaa nähdä käsittelylaitoksen varastossa olevat vapaat lavat | Pysty varaamaan lavat | 
|Kuljetusliike | Haluaa varata tyhjiä lavoja | Voi kuljettaa kuorman omaan varastoon | 
|Kuljetusliike |Haluaa nähdä yhteenvedon | Pystyy laskuttamaan yritystä|
|Kuljetusliike | Haluaa nähdä **ja muokata** lavasäilytyshinta | Pystyy laskuttamaan yritystä ajantasaisesti |
| | | | 


## Tietokanta
#### Luokkakaavio
![batteryV2-luokkakaavio drawio](https://github.com/softalaRyhma2/SR2/assets/71691245/e891cb36-1515-4cb8-ac70-edafe5854788)
#### Javakaavio
![javachart_battery-v2 drawio(1)](https://github.com/softalaRyhma2/SR2/assets/77851668/d7458bec-b2ba-4343-99cc-91eca5657210)




<!--
Järjestelmään säilöttävä ja siinä käsiteltävät tiedot ja niiden väliset suhteet
kuvataan käsitekaaviolla. Käsitemalliin sisältyy myös taulujen välisten viiteyhteyksien ja avainten
määritykset. Tietokanta kuvataan käyttäen jotain kuvausmenetelmää, joko ER-kaaviota ja UML-luokkakaaviota.

Lisäksi kukin järjestelmän tietoelementti ja sen attribuutit kuvataan
tietohakemistossa. Tietohakemisto tarkoittaa yksinkertaisesti vain jokaisen elementin (taulun) ja niiden
attribuuttien (kentät/sarakkeet) listausta ja lyhyttä kuvausta esim. tähän tyyliin:
-->
## Tietohakemisto

> ### _Company_
> _Company-taulu sisältää järjestelmää käyttävän organisaation tiedot (nimi). Organisaatio voi olla: yritys, käsittelylaitos ja kuljetusliike._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> companyId | Long PK | Organisaation id-tunniste
> companyName | varchar(50) | Organisaation nimi

> ### _User_
> _User-taulu sisältää järjestelmää käyttävän käyttäjän tiedot. Käyttäjä liittyy organisaatioon. Yksi käyttäjä voi kulua vain yhteen organisaatioon, mutta organisaatiossa voi olla useampi käyttäjä. Eri organisaatioiden käyttäjillä on eri käyttöoikeudet._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> userId | Long PK | Käyttäjän id-tunniste.
> companyId | int FK | Organisaation, johon käyttäjä kuluu id-tunniste. Viittaus [_company_](#company)-tauluun.
> userName | varchar(50) | Käyttäjänimi.
> userPassword | varchar(255) | Käyttäjän salasana.

> ### _Invoice_
> _Invoice-taulu sisältää laskutustiedot (laskunumero, laskuttaja, kokonaissumma, päivämäärä)._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> invoiceId | Long PK | Laskun id-tunniste.
> companyId | int FK | Organisaation id-tunniste, viittaus [_company_](#company)-tauluun.
> totalSum | decimal(5,2) | Laskun loppusumma.
> invoiceDate | Date | Laskun päivämäärä.

> ### _Stock_
> _Stock-taulu sisältää varastotiedot. Yhteen varastoriviin kuuluu vain yhden organisaation (käsittelylaitos/kuljetusliike) varastotiedot tiettynä päivänä._
>
> Kenttä | Tyyppi | Kuvaus
> ------ | ------ | ------
> stockId | Long PK | Varastotietojen rivin id-tunniste.
> invoiceId | int FK | Laskun id, viittaus  [_invoice_](#invoice)-tauluun.
> quantity | int |  Varaston kokonaislavamäärä.
> available | int | Varaston varattavissa oleva lavamäärä.
> price | decimal(7,2) | Varastointihinta per päivä.
> date | Date | Varastoinnin päivämäärä.

> ### _Reservation_
> _Reservation-taulu sisältää tietyn käyttäjän käsittelylaitokselta tekemän varauksen tietoja, kuten varattu lavamäärä, varauspäivämäärä, onko varaus noudettu. Kun varaus on noudettu käsittelylaitokselta, lavamäärä siirtyy kuljetusliikkeen varastoon._
>
> Kenttä | Tyyppi | Kuvaus |
> ---|---|---|
> reservationId | Long PK | Varauksen id-tunniste.
> stockId | int FK | Varastorivin id-tunniste, viittaus [_stock_](#stock)-tauluun.
> companyId | int FK | Varausta tehneen kuljetusliikkeen id-tunniste, viittaus [_company_](#company)-tauluun.
> reservedQuantity | int | Varattu lavamäärä.
> reservationDate | Date | Varauksen päivämäärä.
> isPickedUp | Boolean | Tieto, onko varaus noudettu, oletuksena kentän arvo on False.


<!--



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
