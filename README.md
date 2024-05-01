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

Web-sovelluksen tarkoituksena on tehdä yritykselle, kuljetusliikkeille ja käsittelylaitokselle yhteinen sovellus, jonka kautta voidaan hallinnoida, varata ja laskuttaa akku- ja paristokierrätykseen tarvittavia lavoja. Ennen tätä sovellusta eri toimijoilla on ollut käytössä vaihtelevia tapoja tehdä näitä toimintoja. Projektin tarkoituksena on tehdä asiakkaalle prototyyppi.
Sovelluksen tekemiseen käytetään JHipster -kehitystyökalua, joka tarjoaa mm. valmiin käyttäjänhallinnan, tietokantarajapinnat sekä tietoturvan.

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

### Käyttöliitymän prototyyppi ja julkaisu

#### Prototyyppi:

- Käyttöliittymän prototyypin toteutus [Figmalla](https://www.figma.com/proto/ro5H0JYtuOTCF1MvtBOJzP/Battery?type=design&node-id=2-3&scaling=min-zoom&page-id=0%3A1&starting-point-node-id=2%3A3).

- [Prototyypin esittelyvideo sprint 1](https://youtu.be/tYGiNWlRKU4?si=zZkSN_4aD26bb1Pi)
- [Prototyypin esittelyvideo sprint 2](https://www.youtube.com/watch?v=XlYQmIB3Y5k)

#### Julkaisu:

- [Linkki Herokussa julkaistuun sovellukseen](https://recser-app-6b6a8f3d45a1.herokuapp.com)

<!--
Esitetään käyttöliittymän tärkeimmät (vain ne!) näkymät sekä niiden väliset siirtymät käyttöliittymäkaaviona.

Jos näkymän tarkoitus ei ole itsestään selvä, se pitää kuvata lyhyesti.
-->

#### Käyttäjäroolit

Projektissamme on neljä käyttäjäroolia: Admin (Ylläpitäjä), Recser (Yritys), Pcenter (Käsittelylaitos) ja Transport (Kuljetusliike). Kukin rooli on suunniteltu tukemaan erilaisia toimintoja ja käyttöoikeuksia järjestelmässä. Admin-rooli on suunniteltu järjestelmän ylläpitäjälle, jolla on laajimmat pääsy- ja hallintaoikeudet.
Recser-rooli on suunnattu yrityksen työntekijöille, jotka käyttävät järjestelmää palvelujensa hallintaan.
Pcenter-rooli keskittyy käsittelylaitoksen varaston hallintaan.
Transport-rooli on suunniteltu kuljetuspalveluiden tarjoajille.

_Ylläpitäjä (Admin) voi:_

<!-- - tarkastella ja muokata järjestelmän Administration-valikkoa -->

- luoda uusia organisaatioita sekä käyttäjiä ja päivittää niiden tietoja.
- nähdä käsittelylaitoksen ja kuljetusliikkeen varastosaldot ja muokata niitä.
- muokata käsittelylaitoksen ja kuljetusliikkeen lavapaikkahintoja.
- nähdä käsittelylaitoksen ja kuljetusliikkeen laskuhistoriat.
- nähdä käsittelylaitoksen ja kuljetusliikkeen organisaatiotietoja.
- tulostaa yhteenvedon (varastosaldo+päivähinta) csv-muodossa.

_Yritys (Recser) voi:_

- luoda uusia organisaatioita sekä käyttäjiä ja päivittää niiden tietoja.
- nähdä käsittelylaitoksen ja kuljetusliikkeen varastosaldot ja muokata niitä.
- muokata käsittelylaitoksen ja kuljetusliikkeen lavapaikkahintoja.
- nähdä käsittelylaitoksen ja kuljetusliikkeen laskuhistoriat.
- nähdä käsittelylaitoksen ja kuljetusliikkeen organisaatiotietoja.
- tulostaa yhteenvedon (varastosaldo+päivähinta) csv-muodossa.

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

| Käyttäjä        | Tekee                                                          | Miksi                                                         |
| --------------- | -------------------------------------------------------------- | ------------------------------------------------------------- |
| Yritys          | Haluaa kirjautua järjestelmään                                 | Näkee käsittelylaitoksen ja kuljetusliikkeiden varastotietoja |
| Yritys          | Haluaa nähdä ja muokata lavasäilytyshinta                      | Tieto järjestelmässä on ajantasainen                          |
| Yritys          | Haluaa nähdä varaston historiaa                                | Voi seurata paljonko keräysvälineitä on ollut ja missä        |
| Yritys          | Haluaa nähdä organisaatiotietoja                               | Pystyy ottamaan yhteyttä organisaatioon                       |
| Käsittelylaitos | Haluaa nähdä yhteenvedon                                       | Pystyy laskuttamaan yritystä                                  |
| Käsittelylaitos | Haluaa kirjautua järjestelmään                                 | Näkee ja pääsee muokkaamaan varastotietoja                    |
| Käsittelylaitos | Haluaa nähdä lavasäilytyshinta                                 | Pystyy laskuttamaan yritystä ajantasaisesti                   |
| Käsittelylaitos | Näkee omia varastotietoja                                      | Pystyy laskuttamaan yritystä                                  |
| Kuljetusliike   | Haluaa kirjautua järjestelmään                                 | Näkee varastotietoja                                          |
| Kuljetusliike   | Näkee omia varastotietoja                                      | Pystyy laskuttamaan yritystä                                  |
| Kuljetusliike   | Haluaa nähdä käsittelylaitoksen varastossa olevat vapaat lavat | Pysty varaamaan lavat                                         |
| Kuljetusliike   | Haluaa varata tyhjiä lavoja                                    | Voi kuljettaa kuorman omaan varastoon                         |
| Kuljetusliike   | Haluaa nähdä yhteenvedon                                       | Pystyy laskuttamaan yritystä                                  |
| Kuljetusliike   | Haluaa nähdä **ja muokata** lavasäilytyshinta                  | Pystyy laskuttamaan yritystä ajantasaisesti                   |
|                 |                                                                |                                                               |

## Tietokanta

#### Luokkakaavio

<!--![batteryV2-luokkakaavio drawio](https://github.com/softalaRyhma2/SR2/assets/71691245/e891cb36-1515-4cb8-ac70-edafe5854788)-->

![batteryV3-1 4 2024 versio3 drawio](https://github.com/softalaRyhma2/SR2/assets/71691245/eb5158a7-302a-46a7-a40a-07325c8dffc7)

#### Javakaavio

![javachart_battery-v3 drawio](https://github.com/softalaRyhma2/SR2/assets/77851668/92e1971a-f4b4-46cc-8832-25952cb2a530)

<!--
Järjestelmään säilöttävä ja siinä käsiteltävät tiedot ja niiden väliset suhteet
kuvataan käsitekaaviolla. Käsitemalliin sisältyy myös taulujen välisten viiteyhteyksien ja avainten
määritykset. Tietokanta kuvataan käyttäen jotain kuvausmenetelmää, joko ER-kaaviota ja UML-luokkakaaviota.

Lisäksi kukin järjestelmän tietoelementti ja sen attribuutit kuvataan
tietohakemistossa. Tietohakemisto tarkoittaa yksinkertaisesti vain jokaisen elementin (taulun) ja niiden
attribuuttien (kentät/sarakkeet) listausta ja lyhyttä kuvausta esim. tähän tyyliin:
-->

## REST-Dokumentaatio

[REST-documentation-SR2.pdf](https://github.com/softalaRyhma2/SR2/src\main\resources\REST-documentation-SR2.pdf)

## Tietohakemisto

> ### _Company_
>
> _Company-taulu sisältää järjestelmää käyttävän organisaation tiedot (nimi, sähköpostiosoite, lisätiedot). Organisaatio voi olla: yritys, käsittelylaitos ja kuljetusliike._
>
> | Kenttä         | Tyyppi       | Kuvaus                                                 |
> | -------------- | ------------ | ------------------------------------------------------ |
> | companyId      | Long PK      | Organisaation id-tunniste.                             |
> | companyName    | varchar(50)  | Organisaation nimi.                                    |
> | companyEmail   | varchar(60)  | Organisaation sähköpostiosoite.                        |
> | companyDetails | varchar(500) | Organisaation lisätiedot, esim. osoite, puhelinnumero. |

> ### _Invoice_
>
> _Invoice-taulu sisältää laskutustiedot (laskunumero, laskuttaja, kokonaissumma, päivämäärä, tieto onko lasku suljettu)._
>
> | Kenttä      | Tyyppi        | Kuvaus                                                                         |
> | ----------- | ------------- | ------------------------------------------------------------------------------ |
> | invoiceId   | Long PK       | Laskun id-tunniste.                                                            |
> | totalSum    | decimal(21,2) | Laskun loppusumma.                                                             |
> | invoiceDate | Date          | Laskun päivämäärä.                                                             |
> | isClosed    | Boolean       | Tieto, onko lasku avoin. Oletuksena kentän arvo on False.                      |
> | companyId   | int FK        | Laskuttavan organisaation id-tunniste, viittaus [_Company_](#company)-tauluun. |

> ### _Stock_
>
> _Stock-taulu sisältää varastotiedot. Yhteen varastoriviin kuuluu vain yhden organisaation (käsittelylaitos/kuljetusliike) varastotiedot tiettynä päivänä._
>
> | Kenttä    | Tyyppi  | Kuvaus                                                                                   |
> | --------- | ------- | ---------------------------------------------------------------------------------------- |
> | stockId   | Long PK | Varastotietojen rivin id-tunniste.                                                       |
> | stockDate | Date    | Varastorivin päivämäärä.                                                                 |
> | invoiceId | int FK  | Laskun id-kenttä johon varastotietoja kirjataan, viittaus [_Invoice_](#invoice)-tauluun. |

> ### _StockItem_
>
> _StockItem-taulu sisältää tuotetiedot, kuten varaston kokonais- sekä varattavissa oleva lavamäärä, päivän säilytyshinta, lavatyyppi. Yhteen tuotetietoriviin kuuluu vain yhden tyypin lavat. Yksi tuotetietorivi voi kuulua yhteen varastoon ja yhdessä varastossa voi olla useampi erityyppinen tuote._
>
> | Kenttä          | Tyyppi        | Kuvaus                                                                                      |
> | --------------- | ------------- | ------------------------------------------------------------------------------------------- |
> | stockItemId     | Long PK       | Tuotetietojen rivin id-tunniste.                                                            |
> | quantity        | int           | Varastossa oleva yhden tyypin kokonaislavamäärä.                                            |
> | available       | int           | Varattavissa oleva yhden tyypin lavamäärä.                                                  |
> | price           | decimal(21,2) | Yhden lavatyypin varastointihinta per päivä.                                                |
> | stockItemTypeId | int FK        | Lavatyypin id-kenttä, viittaus [_StockItemType_](#stockitemtype)-tauluun.                   |
> | stockId         | int FK        | Varaston id-kenttä, johon kuuluu tietyntyyppiset lavat, viittaus [_Stock_](#stock)-tauluun. |

> ### _StockItemType_
>
> _StockItemType-taulu sisältää eri lavatyyppejä._
>
> | Kenttä   | Tyyppi  | Kuvaus                  |
> | -------- | ------- | ----------------------- |
> | id       | Long PK | Lavatyypin id-tunniste. |
> | typeName | String  | Lavatyyppin nimi.       |

> ### _Reservation_
>
> _Reservation-taulu sisältää käsittelylaitoksen varastolta tekemän varauksen tietoja, kuten varauspäivämäärä ja onko varaus noudettu. Yhteen varaukseen voi kuulua useampi varausrivi. Kun varaus on noudettu käsittelylaitokselta, lavamäärä siirtyy kuljetusliikkeen varastoon._
>
> | Kenttä          | Tyyppi  | Kuvaus                                                        |
> | --------------- | ------- | ------------------------------------------------------------- |
> | reservationId   | Long PK | Varauksen id-tunniste.                                        |
> | reservationDate | Date    | Varauksen päivämäärä.                                         |
> | isPickedUp      | Boolean | Tieto, onko varaus noudettu. Oletuksena kentän arvo on False. |

<!-- > | companyId        | int FK  | Varausta tehneen kuljetusliikkeen id-tunniste, viittaus [_Company_](#company)-tauluun. |-->

> ### _ReservedItem_
>
> _ReservedItem-taulu sisältää yhden tuotteen varaustietoja. Varausrivi sisältää tietoa mihin varaukseen se kuuluu, minkätyyppinen tuote ja määrä on varattu, kuka käyttäjä on luonut varausrivin. Yksi varausrivi kuuluu vain yhteen varaukseen._
>
> | Kenttä         | Tyyppi  | Kuvaus                                                                                              |
> | -------------- | ------- | --------------------------------------------------------------------------------------------------- |
> | reservedItemId | Long PK | Varausrivin id-tunniste.                                                                            |
> | stockItemId    | int FK  | Varatun tuotteen id-tunniste, viittaus [_StockItem_](#stockitem)-tauluun.                           |
> | quantity       | int     | Varattu lavamäärä.                                                                                  |
> | reservationId  | int FK  | Varauksen id-tunniste, johon varattu tuote liittyy. Viittaus [_Reservation_](#reservation)-tauluun. |
> | userId         | int FK  | Käyttäjän id-tunniste, joka on varannut tuotteen. Viittaus [_User_](#user)-tauluun.                 |

#### JHipster automaattisesti generoidut taulut:

> ### _User_
>
> _User-taulu sisältää järjestelmää käyttävän käyttäjän tiedot. Käyttäjä liittyy organisaatioon. Yksi käyttäjä voi kulua vain yhteen organisaatioon ja organisaatiossa voi olla yksi käyttäjä. Eri organisaatioiden käyttäjillä on eri käyttöoikeudet._
>
> | Kenttä       | Tyyppi       | Kuvaus                                                                                                                                                                                                                            |
> | ------------ | ------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
> | id           | Long PK      | Käyttäjän id-tunniste.                                                                                                                                                                                                            |
> | companyId    | int FK       | Organisaation, johon käyttäjä kuluu id-tunniste. Viittaus [_Company_](#company)-tauluun.                                                                                                                                          |
> | login        | varchar(50)  | Käyttäjänimi.                                                                                                                                                                                                                     |
> | passwordHash | varchar(60)  | Käyttäjän salasana.                                                                                                                                                                                                               |
> | firstName    | varchar(50)  | Käyttäjän etunimi.                                                                                                                                                                                                                |
> | lastName     | varchar(50)  | Käyttäjän sukunimi.                                                                                                                                                                                                               |
> | email        | varchar(254) | Sähköpostiosoite.                                                                                                                                                                                                                 |
> | activated    | boolean      | Tieto, onko tili aktiivinen. Oletusarvo on False. Tili aktivoidaan luonnin jälkeen tai järjestelmävalvoja määrittelee tilin tilan erikseen.                                                                                       |
> | langKey      | varchar(10)  | Käyttäjän käyttämä kieli järjestelmässä, englanti tai suomi.                                                                                                                                                                      |
> | resetKey     | varchar(20)  | Tilin aktivointiavain, joka lähetetään rekisteröinnin tai salasanan palautuspyynnön yhteydessä käyttäjälle sähköpostitse. Sen avulla pystyy luomaan uuden salasanan tilille. Aktivoinnin jälkeen kentän arvo muuttuu _null_:iksi. |
> | resetDate    | Timestamp    |                                                                                                                                                                                                                                   |

> ### _AbstractAuditingEntity_
>
> _AbstractAuditingEntity-taulu sisältää lisätietoja käyttäjän tilin luonnista ja tietojen muokkauksesta (kuka ja milloin on tehnyt muutoksia)._
> Kenttä | Tyyppi | Kuvaus |
> ------ | ------ | ------ |
> | createdBy | varchar(50) | Tieto, kuka on luonut käyttäjän tunnukset. |
> | createdDate | Timestamp | Tilin luontipäivämäärä. |
> | lastModifiedBy | varchar(50) | Tilin tietojen muokkaajan käyttäjänimi. Tilin aktivoinnin yhteydessä luodun salasanan tallentamisen tekijänä näkyy _anonymousUser_. |
> | lastModifiedDate | Timestamp | Tilin tietojen viimeisen muokkauksen päivämäärä ja kellonaika. |

> ### _Authority_
>
> _Authority-taulu sisältää rooleja. Admin, Recser, Pcenter, Transport._
> Kenttä | Tyyppi | Kuvaus |
> ------ | ------ | ------ |
> name | varchar(50) | Roolin nimi.

> ### _JhiUserAuthority_
>
> _JHIUserAuthority-taulu sisältää tietoa käyttäjien rooleista. Käyttäjällä voi olla yksi tai useampi rooli._
> Kenttä | Tyyppi | Kuvaus |
> ------ | ------ | ------ |
> userId | int FK | Käyttäjän id-tunniste. Viittaus [_User_](#user)-tauluun.
> authorityName | varchar(50) FK | Roolin nimi. Viittaus [_Authority_](#authority)-tauluun.

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
