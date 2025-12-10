package com.emts.smart_attendance_system.dataset;

import com.emts.smart_attendance_system.entities.AcademicMember;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * *******************************************************************
 * File: MemberSet.java
 * Package: com.emts.smart_attendance_system.dataset
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
public class MemberSet {

    private static final String ADMIN = "Admin";
    private static final String USER = "User";

    public Flux<AcademicMember> studentSet(){
        return Flux.just(
                AcademicMember.builder().firstName("Ahmed").lastName("Hassan").birthdate(LocalDate.of(2004, 5, 15)).phone("01001234501").username("AhmedHassan").passwordHash("01001234501").universityNumber("UNI-2004-5").deviceId("").email("ahmed.hassan@student.edu").build(),
                AcademicMember.builder().firstName("Fatima").lastName("Ali").birthdate(LocalDate.of(2003, 8, 22)).phone("01001234502").username("FatimaAli").passwordHash("01001234502").universityNumber("UNI-2003-8").deviceId("").email("fatima.ali@student.edu").build(),
                AcademicMember.builder().firstName("Mohamed").lastName("Ibrahim").birthdate(LocalDate.of(2004, 3, 10)).phone("01001234503").username("MohamedIbrahim").passwordHash("01001234503").universityNumber("UNI-2004-3").deviceId("").email("mohamed.ibrahim@student.edu").build(),
                AcademicMember.builder().firstName("Noor").lastName("Ahmed").birthdate(LocalDate.of(2005, 1, 7)).phone("01001234504").username("NoorAhmed").passwordHash("01001234504").universityNumber("UNI-2005-1").deviceId("").email("noor.ahmed@student.edu").build(),
                AcademicMember.builder().firstName("Khaled").lastName("Omar").birthdate(LocalDate.of(2003, 12, 5)).phone("01001234505").username("KhaledOmar").passwordHash("01001234505").universityNumber("UNI-2003-12").deviceId("").email("khaled.omar@student.edu").build(),
                AcademicMember.builder().firstName("Layla").lastName("Mahmoud").birthdate(LocalDate.of(2004, 7, 18)).phone("01001234506").username("LaylaMahmoud").passwordHash("01001234506").universityNumber("UNI-2004-7").deviceId("").email("layla.mahmoud@student.edu").build(),
                AcademicMember.builder().firstName("Hassan").lastName("Saleh").birthdate(LocalDate.of(2005, 2, 14)).phone("01001234507").username("HasanSaleh").passwordHash("01001234507").universityNumber("UNI-2005-2").deviceId("").email("hassan.saleh@student.edu").build(),
                AcademicMember.builder().firstName("Rania").lastName("Karim").birthdate(LocalDate.of(2003, 9, 25)).phone("01001234508").username("RaniaKarim").passwordHash("01001234508").universityNumber("UNI-2003-9").deviceId("").email("rania.karim@student.edu").build(),
                AcademicMember.builder().firstName("Youssef").lastName("Adel").birthdate(LocalDate.of(2004, 6, 12)).phone("01001234509").username("YoussefAdel").passwordHash("01001234509").universityNumber("UNI-2004-6").deviceId("").email("youssef.adel@student.edu").build(),
                AcademicMember.builder().firstName("Amira").lastName("Hassan").birthdate(LocalDate.of(2005, 4, 8)).phone("01001234510").username("AmiraHassan").passwordHash("01001234510").universityNumber("UNI-2005-4").deviceId("").email("amira.hassan@student.edu").build(),
                AcademicMember.builder().firstName("Samir").lastName("Nasser").birthdate(LocalDate.of(2003, 11, 30)).phone("01001234511").username("SamirNasser").passwordHash("01001234511").universityNumber("UNI-2003-11").deviceId("").email("samir.nasser@student.edu").build(),
                AcademicMember.builder().firstName("Hana").lastName("Walid").birthdate(LocalDate.of(2004, 10, 19)).phone("01001234512").username("HanaWalid").passwordHash("01001234512").universityNumber("UNI-2004-10").deviceId("").email("hana.walid@student.edu").build(),
                AcademicMember.builder().firstName("Tariq").lastName("Farah").birthdate(LocalDate.of(2005, 3, 6)).phone("01001234513").username("TariqFarah").passwordHash("01001234513").universityNumber("UNI-2005-3").deviceId("").email("tariq.farah@student.edu").build(),
                AcademicMember.builder().firstName("Sara").lastName("Rashid").birthdate(LocalDate.of(2003, 7, 28)).phone("01001234514").username("SaraRashid").passwordHash("01001234514").universityNumber("UNI-2003-7").deviceId("").email("sara.rashid@student.edu").build(),
                AcademicMember.builder().firstName("Basem").lastName("Samir").birthdate(LocalDate.of(2004, 2, 9)).phone("01001234515").username("BasemSamir").passwordHash("01001234515").universityNumber("UNI-2004-2").deviceId("").email("basem.samir@student.edu").build(),
                AcademicMember.builder().firstName("Dina").lastName("Kareem").birthdate(LocalDate.of(2005, 5, 21)).phone("01001234516").username("DinaKareem").passwordHash("01001234516").universityNumber("UNI-2005-5").deviceId("").email("dina.kareem@student.edu").build(),
                AcademicMember.builder().firstName("Omar").lastName("Sayed").birthdate(LocalDate.of(2003, 4, 11)).phone("01001234517").username("OmarSayed").passwordHash("01001234517").universityNumber("UNI-2003-4").deviceId("").email("omar.sayed@student.edu").build(),
                AcademicMember.builder().firstName("Noura").lastName("Ibrahim").birthdate(LocalDate.of(2004, 8, 26)).phone("01001234518").username("NouraIbrahim").passwordHash("01001234518").universityNumber("UNI-2004-8").deviceId("").email("noura.ibrahim@student.edu").build(),
                AcademicMember.builder().firstName("Wael").lastName("Hassan").birthdate(LocalDate.of(2005, 6, 17)).phone("01001234519").username("WaelHassan").passwordHash("01001234519").universityNumber("UNI-2005-6").deviceId("").email("wael.hassan@student.edu").build(),
                AcademicMember.builder().firstName("Mariam").lastName("Noor").birthdate(LocalDate.of(2003, 6, 24)).phone("01001234520").username("MariamNoor").passwordHash("01001234520").universityNumber("UNI-2003-6").deviceId("").email("mariam.noor@student.edu").build(),
                AcademicMember.builder().firstName("Ibrahim").lastName("Karim").birthdate(LocalDate.of(2004, 9, 13)).phone("01001234521").username("IbrahimKarim").passwordHash("01001234521").universityNumber("UNI-2004-9").deviceId("").email("ibrahim.karim@student.edu").build(),
                AcademicMember.builder().firstName("Lina").lastName("Walid").birthdate(LocalDate.of(2005, 7, 20)).phone("01001234522").username("LinaWalid").passwordHash("01001234522").universityNumber("UNI-2005-7").deviceId("").email("lina.walid@student.edu").build(),
                AcademicMember.builder().firstName("Jamal").lastName("Ahmed").birthdate(LocalDate.of(2003, 10, 3)).phone("01001234523").username("JamalAhmed").passwordHash("01001234523").universityNumber("UNI-2003-10").deviceId("").email("jamal.ahmed@student.edu").build(),
                AcademicMember.builder().firstName("Yasmin").lastName("Samir").birthdate(LocalDate.of(2004, 1, 31)).phone("01001234524").username("YasminSamir").passwordHash("01001234524").universityNumber("UNI-2004-1").deviceId("").email("yasmin.samir@student.edu").build(),
                AcademicMember.builder().firstName("Karim").lastName("Rashid").birthdate(LocalDate.of(2005, 8, 16)).phone("01001234525").username("KarimRashid").passwordHash("01001234525").universityNumber("UNI-2005-8").deviceId("").email("karim.rashid@student.edu").build(),
                AcademicMember.builder().firstName("Hend").lastName("Adel").birthdate(LocalDate.of(2003, 5, 2)).phone("01001234526").username("HendAdel").passwordHash("01001234526").universityNumber("UNI-2003-5").deviceId("").email("hend.adel@student.edu").build(),
                AcademicMember.builder().firstName("Ashraf").lastName("Nasser").birthdate(LocalDate.of(2004, 11, 23)).phone("01001234527").username("AshrafNasser").passwordHash("01001234527").universityNumber("UNI-2004-11").deviceId("").email("ashraf.nasser@student.edu").build(),
                AcademicMember.builder().firstName("Salma").lastName("Karim").birthdate(LocalDate.of(2005, 9, 10)).phone("01001234528").username("SalmaKarim").passwordHash("01001234528").universityNumber("UNI-2005-9").deviceId("").email("salma.karim@student.edu").build(),
                AcademicMember.builder().firstName("Samy").lastName("Walid").birthdate(LocalDate.of(2003, 12, 7)).phone("01001234529").username("SamyWalid").passwordHash("01001234529").universityNumber("UNI-2003-12").deviceId("").email("samy.walid@student.edu").build(),
                AcademicMember.builder().firstName("Nadia").lastName("Sayed").birthdate(LocalDate.of(2004, 4, 19)).phone("01001234530").username("NadiaSayed").passwordHash("01001234530").universityNumber("UNI-2004-4").deviceId("").email("nadia.sayed@student.edu").build(),
                AcademicMember.builder().firstName("Amin").lastName("Farah").birthdate(LocalDate.of(2005, 11, 27)).phone("01001234531").username("AminFarah").passwordHash("01001234531").universityNumber("UNI-2005-11").deviceId("").email("amin.farah@student.edu").build(),
                AcademicMember.builder().firstName("Zainab").lastName("Hassan").birthdate(LocalDate.of(2003, 8, 14)).phone("01001234532").username("ZainabHassan").passwordHash("01001234532").universityNumber("UNI-2003-8").deviceId("").email("zainab.hassan@student.edu").build(),
                AcademicMember.builder().firstName("Waleed").lastName("Ali").birthdate(LocalDate.of(2004, 12, 29)).phone("01001234533").username("WaleedAli").passwordHash("01001234533").universityNumber("UNI-2004-12").deviceId("").email("waleed.ali@student.edu").build(),
                AcademicMember.builder().firstName("Lena").lastName("Omar").birthdate(LocalDate.of(2005, 10, 4)).phone("01001234534").username("LenaOmar").passwordHash("01001234534").universityNumber("UNI-2005-10").deviceId("").email("lena.omar@student.edu").build(),
                AcademicMember.builder().firstName("Rizk").lastName("Ibrahim").birthdate(LocalDate.of(2003, 3, 22)).phone("01001234535").username("RizkIbrahim").passwordHash("01001234535").universityNumber("UNI-2003-3").deviceId("").email("rizk.ibrahim@student.edu").build(),
                AcademicMember.builder().firstName("Rana").lastName("Mahmoud").birthdate(LocalDate.of(2004, 7, 30)).phone("01001234536").username("RanaMahmoud").passwordHash("01001234536").universityNumber("UNI-2004-7").deviceId("").email("rana.mahmoud@student.edu").build(),
                AcademicMember.builder().firstName("Issa").lastName("Saleh").birthdate(LocalDate.of(2005, 2, 5)).phone("01001234537").username("IssaSaleh").passwordHash("01001234537").universityNumber("UNI-2005-2").deviceId("").email("issa.saleh@student.edu").build(),
                AcademicMember.builder().firstName("Hiba").lastName("Noor").birthdate(LocalDate.of(2003, 9, 18)).phone("01001234538").username("HibaNoor").passwordHash("01001234538").universityNumber("UNI-2003-9").deviceId("").email("hiba.noor@student.edu").build(),
                AcademicMember.builder().firstName("Tarek").lastName("Karim").birthdate(LocalDate.of(2004, 6, 11)).phone("01001234539").username("TarekKarim").passwordHash("01001234539").universityNumber("UNI-2004-6").deviceId("").email("tarek.karim@student.edu").build(),
                AcademicMember.builder().firstName("Amal").lastName("Adel").birthdate(LocalDate.of(2005, 4, 24)).phone("01001234540").username("AmalAdel").passwordHash("01001234540").universityNumber("UNI-2005-4").deviceId("").email("amal.adel@student.edu").build(),
                AcademicMember.builder().firstName("Nasser").lastName("Nasser").birthdate(LocalDate.of(2003, 11, 9)).phone("01001234541").username("NasserNasser").passwordHash("01001234541").universityNumber("UNI-2003-11").deviceId("").email("nasser.nasser@student.edu").build(),
                AcademicMember.builder().firstName("Noor").lastName("Walid").birthdate(LocalDate.of(2004, 10, 12)).phone("01001234542").username("NoorWalid").passwordHash("01001234542").universityNumber("UNI-2004-10").deviceId("").email("noor.walid@student.edu").build(),
                AcademicMember.builder().firstName("Saif").lastName("Farah").birthdate(LocalDate.of(2005, 3, 15)).phone("01001234543").username("SaifFarah").passwordHash("01001234543").universityNumber("UNI-2005-3").deviceId("").email("saif.farah@student.edu").build(),
                AcademicMember.builder().firstName("Mona").lastName("Rashid").birthdate(LocalDate.of(2003, 7, 2)).phone("01001234544").username("MonaRashid").passwordHash("01001234544").universityNumber("UNI-2003-7").deviceId("").email("mona.rashid@student.edu").build(),
                AcademicMember.builder().firstName("Mazen").lastName("Samir").birthdate(LocalDate.of(2004, 2, 21)).phone("01001234545").username("MazenSamir").passwordHash("01001234545").universityNumber("UNI-2004-2").deviceId("").email("mazen.samir@student.edu").build(),
                AcademicMember.builder().firstName("Sima").lastName("Kareem").birthdate(LocalDate.of(2005, 5, 8)).phone("01001234546").username("SimaKareem").passwordHash("01001234546").universityNumber("UNI-2005-5").deviceId("").email("sima.kareem@student.edu").build(),
                AcademicMember.builder().firstName("Hamid").lastName("Sayed").birthdate(LocalDate.of(2003, 4, 27)).phone("01001234547").username("HamidSayed").passwordHash("01001234547").universityNumber("UNI-2003-4").deviceId("").email("hamid.sayed@student.edu").build(),
                AcademicMember.builder().firstName("Nadia").lastName("Ibrahim").birthdate(LocalDate.of(2004, 8, 6)).phone("01001234548").username("NadiaIbrahim").passwordHash("01001234548").universityNumber("UNI-2004-8").deviceId("").email("nadia.ibrahim@student.edu").build(),
                AcademicMember.builder().firstName("Rayan").lastName("Hassan").birthdate(LocalDate.of(2005, 6, 28)).phone("01001234549").username("RayanHassan").passwordHash("01001234549").universityNumber("UNI-2005-6").deviceId("").email("rayan.hassan@student.edu").build(),
                AcademicMember.builder().firstName("Hanaa").lastName("Noor").birthdate(LocalDate.of(2003, 6, 13)).phone("01001234550").username("HanaaNoor").passwordHash("01001234550").universityNumber("UNI-2003-6").deviceId("").email("hanaa.noor@student.edu").build(),
                AcademicMember.builder().firstName("Raed").lastName("Karim").birthdate(LocalDate.of(2004, 9, 1)).phone("01001234551").username("RaedKarim").passwordHash("01001234551").universityNumber("UNI-2004-9").deviceId("").email("raed.karim@student.edu").build(),
                AcademicMember.builder().firstName("Lara").lastName("Walid").birthdate(LocalDate.of(2005, 7, 19)).phone("01001234552").username("LaraWalid").passwordHash("01001234552").universityNumber("UNI-2005-7").deviceId("").email("lara.walid@student.edu").build(),
                AcademicMember.builder().firstName("Jamel").lastName("Ahmed").birthdate(LocalDate.of(2003, 10, 26)).phone("01001234553").username("JamelAhmed").passwordHash("01001234553").universityNumber("UNI-2003-10").deviceId("").email("jamel.ahmed@student.edu").build(),
                AcademicMember.builder().firstName("Yasmin").lastName("Samir").birthdate(LocalDate.of(2004, 1, 17)).phone("01001234554").username("YasminSamir").passwordHash("01001234554").universityNumber("UNI-2004-1").deviceId("").email("yasmin.samir.s@student.edu").build(),
                AcademicMember.builder().firstName("Karim").lastName("Rashid").birthdate(LocalDate.of(2005, 8, 3)).phone("01001234555").username("KarimRashid").passwordHash("01001234555").universityNumber("UNI-2005-8").deviceId("").email("karim.rashid.s@student.edu").build(),
                AcademicMember.builder().firstName("Heba").lastName("Adel").birthdate(LocalDate.of(2003, 5, 20)).phone("01001234556").username("HebaAdel").passwordHash("01001234556").universityNumber("UNI-2003-5").deviceId("").email("heba.adel@student.edu").build(),
                AcademicMember.builder().firstName("Amr").lastName("Nasser").birthdate(LocalDate.of(2004, 11, 8)).phone("01001234557").username("AmrNasser").passwordHash("01001234557").universityNumber("UNI-2004-11").deviceId("").email("amr.nasser@student.edu").build(),
                AcademicMember.builder().firstName("Sifa").lastName("Karim").birthdate(LocalDate.of(2005, 9, 25)).phone("01001234558").username("SifaKarim").passwordHash("01001234558").universityNumber("UNI-2005-9").deviceId("").email("sifa.karim@student.edu").build(),
                AcademicMember.builder().firstName("Saad").lastName("Walid").birthdate(LocalDate.of(2003, 12, 11)).phone("01001234559").username("SaadWalid").passwordHash("01001234559").universityNumber("UNI-2003-12").deviceId("").email("saad.walid@student.edu").build(),
                AcademicMember.builder().firstName("Nadia").lastName("Sayed").birthdate(LocalDate.of(2004, 4, 6)).phone("01001234560").username("NadiaSayed").passwordHash("01001234560").universityNumber("UNI-2004-4").deviceId("").email("nadia.sayed.s@student.edu").build(),
                AcademicMember.builder().firstName("Ameen").lastName("Farah").birthdate(LocalDate.of(2005, 11, 14)).phone("01001234561").username("AmeenFarah").passwordHash("01001234561").universityNumber("UNI-2005-11").deviceId("").email("ameen.farah@student.edu").build(),
                AcademicMember.builder().firstName("Zina").lastName("Hassan").birthdate(LocalDate.of(2003, 8, 30)).phone("01001234562").username("ZinaHassan").passwordHash("01001234562").universityNumber("UNI-2003-8").deviceId("").email("zina.hassan@student.edu").build(),
                AcademicMember.builder().firstName("Wail").lastName("Ali").birthdate(LocalDate.of(2004, 12, 16)).phone("01001234563").username("WailAli").passwordHash("01001234563").universityNumber("UNI-2004-12").deviceId("").email("wail.ali@student.edu").build(),
                AcademicMember.builder().firstName("Leila").lastName("Omar").birthdate(LocalDate.of(2005, 10, 20)).phone("01001234564").username("LeilaOmar").passwordHash("01001234564").universityNumber("UNI-2005-10").deviceId("").email("leila.omar@student.edu").build(),
                AcademicMember.builder().firstName("Rafiq").lastName("Ibrahim").birthdate(LocalDate.of(2003, 3, 9)).phone("01001234565").username("RafiqIbrahim").passwordHash("01001234565").universityNumber("UNI-2003-3").deviceId("").email("rafiq.ibrahim@student.edu").build(),
                AcademicMember.builder().firstName("Rima").lastName("Mahmoud").birthdate(LocalDate.of(2004, 7, 23)).phone("01001234566").username("RimaMahmoud").passwordHash("01001234566").universityNumber("UNI-2004-7").deviceId("").email("rima.mahmoud@student.edu").build(),
                AcademicMember.builder().firstName("Islam").lastName("Saleh").birthdate(LocalDate.of(2005, 2, 18)).phone("01001234567").username("IslamSaleh").passwordHash("01001234567").universityNumber("UNI-2005-2").deviceId("").email("islam.saleh@student.edu").build(),
                AcademicMember.builder().firstName("Hana").lastName("Noor").birthdate(LocalDate.of(2003, 9, 4)).phone("01001234568").username("HanaNoor").passwordHash("01001234568").universityNumber("UNI-2003-9").deviceId("").email("hana.noor.s@student.edu").build()
        );
    }

    public Flux<AcademicMember> teacherSet(){
        return Flux.just(
                AcademicMember.builder().firstName("Ahmed").lastName("Karim").birthdate(LocalDate.of(1980, 3, 15)).phone("01012345601").username("AhmedKarim").passwordHash("01012345601").universityNumber("UNI-1980-3").deviceId("").email("ahmed.karim@teacher.edu").build(),
                AcademicMember.builder().firstName("Fatima").lastName("Hassan").birthdate(LocalDate.of(1982, 7, 22)).phone("01012345602").username("FatimaHassan").passwordHash("01012345602").universityNumber("UNI-1982-7").deviceId("").email("fatima.hassan@teacher.edu").build(),
                AcademicMember.builder().firstName("Mohamed").lastName("Ali").birthdate(LocalDate.of(1979, 11, 10)).phone("01012345603").username("MohamedAli").passwordHash("01012345603").universityNumber("UNI-1979-11").deviceId("").email("mohamed.ali@teacher.edu").build(),
                AcademicMember.builder().firstName("Hana").lastName("Walid").birthdate(LocalDate.of(1981, 2, 5)).phone("01012345604").username("HanaWalid").passwordHash("01012345604").universityNumber("UNI-1981-2").deviceId("").email("hana.walid@teacher.edu").build(),
                AcademicMember.builder().firstName("Tariq").lastName("Saleh").birthdate(LocalDate.of(1983, 6, 18)).phone("01012345605").username("TariqSaleh").passwordHash("01012345605").universityNumber("UNI-1983-6").deviceId("").email("tariq.saleh@teacher.edu").build(),
                AcademicMember.builder().firstName("Layla").lastName("Nour").birthdate(LocalDate.of(1980, 9, 28)).phone("01012345606").username("LaylaNour").passwordHash("01012345606").universityNumber("UNI-1980-9").deviceId("").email("layla.nour@teacher.edu").build(),
                AcademicMember.builder().firstName("Karim").lastName("Farah").birthdate(LocalDate.of(1978, 4, 12)).phone("01012345607").username("KarimFarah").passwordHash("01012345607").universityNumber("UNI-1978-4").deviceId("").email("karim.farah@teacher.edu").build(),
                AcademicMember.builder().firstName("Noor").lastName("Ahmed").birthdate(LocalDate.of(1981, 8, 3)).phone("01012345608").username("NoorAhmed").passwordHash("01012345608").universityNumber("UNI-1981-8").deviceId("").email("noor.ahmed@teacher.edu").build(),
                AcademicMember.builder().firstName("Samir").lastName("Ibrahim").birthdate(LocalDate.of(1982, 1, 25)).phone("01012345609").username("SamirIbrahim").passwordHash("01012345609").universityNumber("UNI-1982-1").deviceId("").email("samir.ibrahim@teacher.edu").build(),
                AcademicMember.builder().firstName("Rania").lastName("Omar").birthdate(LocalDate.of(1979, 10, 14)).phone("01012345610").username("RaniaOmar").passwordHash("01012345610").universityNumber("UNI-1979-10").deviceId("").email("rania.omar@teacher.edu").build(),
                AcademicMember.builder().firstName("Waleed").lastName("Karim").birthdate(LocalDate.of(1980, 5, 19)).phone("01012345611").username("WaleedKarim").passwordHash("01012345611").universityNumber("UNI-1980-5").deviceId("").email("waleed.karim@teacher.edu").build(),
                AcademicMember.builder().firstName("Amira").lastName("Sayed").birthdate(LocalDate.of(1983, 12, 8)).phone("01012345612").username("AmiraSayed").passwordHash("01012345612").universityNumber("UNI-1983-12").deviceId("").email("amira.sayed@teacher.edu").build(),
                AcademicMember.builder().firstName("Rashid").lastName("Nasser").birthdate(LocalDate.of(1981, 3, 21)).phone("01012345613").username("RashidNasser").passwordHash("01012345613").universityNumber("UNI-1981-3").deviceId("").email("rashid.nasser@teacher.edu").build(),
                AcademicMember.builder().firstName("Zainab").lastName("Hassan").birthdate(LocalDate.of(1978, 7, 11)).phone("01012345614").username("ZainabHassan").passwordHash("01012345614").universityNumber("UNI-1978-7").deviceId("").email("zainab.hassan@teacher.edu").build(),
                AcademicMember.builder().firstName("Youssef").lastName("Adel").birthdate(LocalDate.of(1982, 9, 6)).phone("01012345615").username("YoussefAdel").passwordHash("01012345615").universityNumber("UNI-1982-9").deviceId("").email("youssef.adel@teacher.edu").build()
        );
    }

    public Flux<AcademicMember> adminSet(){
        return Flux.just(
                AcademicMember.builder().firstName(ADMIN).lastName("One").birthdate(LocalDate.of(1975, 2, 10)).phone("01012346701").username("AdminOne").passwordHash("01012346701").universityNumber("UNI-1975-2").deviceId("").email("admin.one@hiet.edu").build(),
                AcademicMember.builder().firstName(ADMIN).lastName("Two").birthdate(LocalDate.of(1976, 5, 20)).phone("01012346702").username("AdminTwo").passwordHash("01012346702").universityNumber("UNI-1976-5").deviceId("").email("admin.two@hiet.edu").build(),
                AcademicMember.builder().firstName(ADMIN).lastName("Three").birthdate(LocalDate.of(1977, 8, 15)).phone("01012346703").username("AdminThree").passwordHash("01012346703").universityNumber("UNI-1977-8").deviceId("").email("admin.three@hiet.edu").build(),
                AcademicMember.builder().firstName(ADMIN).lastName("Four").birthdate(LocalDate.of(1975, 11, 30)).phone("01012346704").username("AdminFour").passwordHash("01012346704").universityNumber("UNI-1975-11").deviceId("").email("admin.four@hiet.edu").build(),
                AcademicMember.builder().firstName(ADMIN).lastName("Five").birthdate(LocalDate.of(1976, 3, 25)).phone("01012346705").username("AdminFive").passwordHash("01012346705").universityNumber("UNI-1976-3").deviceId("").email("admin.five@hiet.edu").build()
        );
    }

    public Flux<AcademicMember> userSet(){
        return Flux.just(
                AcademicMember.builder().firstName(USER).lastName("One").birthdate(LocalDate.of(1990, 1, 5)).phone("01012347801").username("UserOne").passwordHash("01012347801").universityNumber("UNI-1990-1").deviceId("").email("user.one@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Two").birthdate(LocalDate.of(1991, 4, 12)).phone("01012347802").username("UserTwo").passwordHash("01012347802").universityNumber("UNI-1991-4").deviceId("").email("user.two@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Three").birthdate(LocalDate.of(1989, 6, 18)).phone("01012347803").username("UserThree").passwordHash("01012347803").universityNumber("UNI-1989-6").deviceId("").email("user.three@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Five").birthdate(LocalDate.of(1990, 11, 8)).phone("01012347805").username("UserFive").passwordHash("01012347805").universityNumber("UNI-1990-11").deviceId("").email("user.five@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Four").birthdate(LocalDate.of(1992, 9, 22)).phone("01012347804").username("UserFour").passwordHash("01012347804").universityNumber("UNI-1992-9").deviceId("").email("user.four@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Six").birthdate(LocalDate.of(1991, 2, 14)).phone("01012347806").username("UserSix").passwordHash("01012347806").universityNumber("UNI-1991-2").deviceId("").email("user.six@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Seven").birthdate(LocalDate.of(1989, 7, 3)).phone("01012347807").username("UserSeven").passwordHash("01012347807").universityNumber("UNI-1989-7").deviceId("").email("user.seven@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Eight").birthdate(LocalDate.of(1992, 10, 17)).phone("01012347808").username("UserEight").passwordHash("01012347808").universityNumber("UNI-1992-10").deviceId("").email("user.eight@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Nine").birthdate(LocalDate.of(1990, 5, 27)).phone("01012347809").username("UserNine").passwordHash("01012347809").universityNumber("UNI-1990-5").deviceId("").email("user.nine@system.edu").build(),
                AcademicMember.builder().firstName(USER).lastName("Ten").birthdate(LocalDate.of(1991, 8, 9)).phone("01012347810").username("UserTen").passwordHash("01012347810").universityNumber("UNI-1991-8").deviceId("").email("user.ten@system.edu").build()
        );
    }
}
