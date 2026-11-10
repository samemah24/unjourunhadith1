# Hadith du jour

Application Android (Kotlin + Jetpack Compose) qui affiche un hadith par jour.

## Fonctionnalités

- Hadith du jour (change chaque jour, fonctionne hors ligne)
- Langues : français, anglais, allemand, espagnol (automatique selon l'appareil, ou au choix)
- Notification quotidienne à l'heure choisie
- Favoris
- Partage (WhatsApp, SMS, etc.)
- Widget d'écran d'accueil
- Thème clair / sombre automatique

## Ouvrir et lancer le projet

1. Installer [Android Studio](https://developer.android.com/studio) (version récente).
2. `File > Open` et choisir le dossier `HadithDuJour`.
3. Attendre la synchronisation Gradle (Android Studio télécharge Gradle et les dépendances).
4. Brancher un téléphone (débogage USB activé) ou lancer un émulateur, puis cliquer sur ▶ **Run**.

Pour obtenir un APK : `Build > Build Bundle(s) / APK(s) > Build APK(s)`.

## Obtenir l'APK

- **Android Studio :** `Build > Build Bundle(s) / APK(s) > Build APK(s)`. Le fichier se trouve dans
  `app/build/outputs/apk/debug/app-debug.apk`. Copie-le sur le téléphone et ouvre-le pour l'installer
  (autoriser l'installation depuis cette source).
- **Sans Android Studio :** mettre le projet sur GitHub. Le fichier `.github/workflows/build-apk.yml`
  compile l'APK automatiquement ; il est à télécharger dans l'onglet *Actions*, section *Artifacts*.

## Ajouter des hadiths

Éditer `app/src/main/assets/hadiths.json` : ajouter un objet avec un `id` unique, le
`narrator`, la `source` et les quatre traductions (`fr`, `en`, `de`, `es`).
Le hadith du jour est choisi en parcourant la liste dans l'ordre, un par jour.

## Structure

- `app/src/main/assets/hadiths.json` : les hadiths et leurs traductions
- `app/src/main/java/com/hadithdujour/app/` : code Kotlin (écrans, notification, widget)
- `app/src/main/res/` : textes traduits, icônes, thèmes, mise en page du widget
