   M S B A M L   `   `   `  ���� B  >WpfApp1, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null!clr-namespace:WpfApp1WpfApp1      WpfApp1.MainWindow   W SPresentationCore, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089R NWindowsBase, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     R NSystem.Xaml, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     'localclr-namespace:WpfApp1     -     �Name$  window�� � Title$ 
MainWindow��$	��677��$	��966�� � Icon$  /Der aller echte Papst.jpg��.��� -    �Name$
 grid��$��10q� � RowDefinitions �� $
��Auto ��� $��* ��� $��* � � ColumnDefinitions �� $	��17* ��� $
��401* ��� $
��259* ��� $
��159* �.+��� $��2��$��1��$��0,1,0,1q� J� RenderTransformOrigin$ 0.506,0.511&�$��Center=�$	��374��.+��� $
��Bold"�.��Neuen Geburtstag hinzufügen�� $
��Left=�$��Vorname���� -    ��Name$ 
txtVorname��   CustomTextBoxStyle#��[  $
��Left=� �� TextWrapping$
 Wrap=�$	��120���� $
��Left=�$��Nachname���� -   $ txtNachname��#��[  $
��Left=�$
 Wrap=�$	��120���� $
��Left=�	 �� Orientation$	 
Horizontal=�.+��� $
��Left=�$��70��$��
0, 3, 3, 0q�.��Tag�� $
��Left=�$��70��$��
0, 3, 3, 0q�.��Monat�� $
��Left=�$��70��$��
0, 3, 3, 0q�.��Jahr�� $
��Left=�$	 
Horizontal=�.+��� -   $ txtTag��#��[  $
 Wrap=�$��70��$��
0, 0, 3, 3q��� -   $ txtMonat��#��[  $
 Wrap=�$��70��$��
0, 0, 3, 3q��� -   $ txtJahr��#��[  $
 Wrap=�$��70��$��
0, 0, 3, 3q��� -   
 ��Name$
 btnAdd��  RoundButtonTemplate#��[ $��Hinzufügen��$
��Left=�$��0, 10, 10, 0q�$	��145��$��37���� -	   $
 	btnUpdate��#��[ $��Änderungen speichern��$
��Left=�${�False��$��0, 10, 10, 0q�$	��145��$��37���� -
   $��lvUsers��$��139,10,0,10q�$��1��$��2��$
��Left=�$��1��$	��511�� � 	Resources  �� 
TargetType �� Property �� Value��%/   '��       �� " ��.���  � ��$
 Left=� �� View �� .P��� $��Vorname�� �� Width$	 120�� �� DisplayMemberBinding �� *	vorname+�� $��Nachname��$	 120�� �� *
nachname+�� $��
Geburtstag��$	 120�� �� *
geburtstag+�� $��Alter��$	 100�� �� *alter+�� $��1��$��3��$��Center=�$��Center=�$	��147��$
��Auto��.+��� -   $
 btnApi��#��[ $��37��$��
Alle laden��$��5q�$	��123���� -   $
 	btnRemove��#��[ $��37��$��Löschen��${�false��$��5q�$	��123���� -   $
 	btnChange��#��[ $��
Bearbeiten��${�false��$��37��$��5q�$	��123���� $��2��$��2��$
��Left=�$	��219��.+��� $
��Bold"�.��Geburtstag suchen�� $
��Left=�$��Vorname���� -   $ searchVorname��#��[  $
��Left=�$
 Wrap=�$	��120���� $
��Left=�$��Nachname���� -   $ searchNachname��#��[  $
��Left=�$
 Wrap=�$	��120���� $
��Left=�$	 
Horizontal=�.+��� $
��Left=�$��70��$��
0, 3, 3, 0q�.��Tag�� $
��Left=�$��70��$��
0, 3, 3, 0q�.��Monat�� $
��Left=�$��70��$��
0, 3, 3, 0q�.��Jahr�� $��Center=�$	 
Horizontal=�.+��� -   $ 	searchTag��#��[  $
 Wrap=�$��70��$��
0, 0, 3, 3q��� -   $ searchMonat��#��[  $
 Wrap=�$��70��$��
0, 0, 3, 3q��� -   $ 
searchJahr��#��[  $
 Wrap=�$��70��$��
0, 0, 3, 3q��� $
��Left=�.��Alter�� -   $ searchAlter��#��[  $
��Left=�$
 Wrap=�$��70���� -   $
 btnFind��#��[ $��Suchen��$
��Left=�$	��123��$��37��$��0, 10, 10, 0q�