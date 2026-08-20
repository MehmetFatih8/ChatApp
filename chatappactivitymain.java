<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">


    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true"
        android:text="Welcome to Fatih Chat App!"
        android:textSize="25sp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"

        app:layout_constraintHeight_percent="0.04"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.03"
        app:layout_constraintWidth_percent="0.8" />



    <EditText
        android:id="@+id/gonderilecekmesajedittext"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:ems="8"
        android:enabled="false"
        android:gravity="bottom"
        android:hint="Gönderilecek mesaj"
        android:inputType="text"
        android:minHeight="60dp"


        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"

        app:layout_constraintHeight_percent="0.065"
        app:layout_constraintHorizontal_bias="0.15"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.95"
        app:layout_constraintWidth_percent="0.5" />

    <Button
        android:id="@+id/gonderbutonu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:enabled="false"
        android:minHeight="57dp"
        android:text="Gönder"
        android:textColor="@color/turuncu"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"

        app:layout_constraintHeight_percent="0.08"
        app:layout_constraintHorizontal_bias="0.85"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.95"
        app:layout_constraintWidth_percent="0.3" />

    <TextView
        android:id="@+id/sohbetgecmisitextview"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:background="@drawable/kenarlik"
        android:gravity="bottom"
        android:paddingLeft="15dp"
        android:paddingTop="15dp"
        android:paddingRight="15dp"
        android:scrollbars="vertical"
        android:text=""
        android:textSize="17sp"
        android:hint="Sohbet geçmişi...\n"
        android:includeFontPadding="false"


        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"


        app:layout_constraintHeight_percent="0.60"
        app:layout_constraintHorizontal_bias="0.4"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.58"
        app:layout_constraintWidth_percent="0.82" />

    <Button
        android:id="@+id/oturumukapatbutonu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:enabled="false"
        android:minHeight="60dp"
        android:text="Oturumu kapat"
        android:textColor="@color/camgobegi"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"

        app:layout_constraintHeight_percent="0.08"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.1"
        app:layout_constraintWidth_percent="0.3" />



</androidx.constraintlayout.widget.ConstraintLayout>