using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Xml;
using Newtonsoft.Json;
using System.Text.Json;
using JsonSerializer = System.Text.Json.JsonSerializer;
using Microsoft.VisualBasic;
using Microsoft.Win32;
using System.Globalization;
using System.Net.Http;
using System.Text.RegularExpressions;
using Newtonsoft.Json.Linq;
using System.Net;
using System.Diagnostics.Metrics;
using System.Text.Json.Nodes;

namespace WpfApp1
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            load();
        }

        private static readonly HttpClient client = new HttpClient();
        private async void btnAdd_Click(object sender, RoutedEventArgs e)
        {
            if(string.IsNullOrEmpty(txtVorname.Text) || string.IsNullOrEmpty(txtNachname.Text) || string.IsNullOrEmpty(txtTag.Text) || string.IsNullOrEmpty(txtMonat.Text) || string.IsNullOrEmpty(txtJahr.Text))
            {
                MessageBox.Show("Bitte füllen Sie alle Felder aus!");
                return;
            }
            try
            {
                int tag = Convert.ToInt32(txtTag.Text);
                int monat = Convert.ToInt32(txtMonat.Text);
                int jahr = Convert.ToInt32(txtJahr.Text);
                DateTime dateTime = DateTime.Parse(jahr + "-" + monat + "-" + tag);
                if (jahr < 1800 || dateTime > DateTime.Now)
                {
                    MessageBox.Show("Bitte gültiges Datum eingeben!");
                    return;
                }
            } catch(Exception ex)
            {
                MessageBox.Show("Bitte gültiges Datum eingeben!");
                return;
            }

            var httpWebRequest = (HttpWebRequest)WebRequest.Create("http://localhost:3001/add");
            httpWebRequest.ContentType = "application/json";
            httpWebRequest.Method = "POST";

            using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
            {
                string json = JsonSerializer.Serialize(new
                {
                    vorname = txtVorname.Text,
                    nachname = txtNachname.Text,
                    tag = txtTag.Text,
                    monat = txtMonat.Text, 
                    jahr = txtJahr.Text
                });

                streamWriter.Write(json);
            }

            var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                var result = streamReader.ReadToEnd();
            }

            load();

            txtVorname.Text = "";
            txtNachname.Text = "";
            txtTag.Text = "";
            txtMonat.Text = "";
            txtJahr.Text = "";
        }

        private async void load()
        {
            HttpClient client = new HttpClient();
            string data = client.GetStringAsync("http://localhost:3001/geburtstage").Result;
            var list = JsonSerializer.Deserialize<List<Person>>(data);
            appendData(list);
        }

        private static HttpClient _httpClient = new HttpClient();



        private void lvUsers_Click(object sender, RoutedEventArgs e)
        {
            var item = (sender as ListView).SelectedItem;
            if (item != null)
            { 
                btnRemove.IsEnabled = true;
                Person s = (Person)lvUsers.SelectedItems[0];
                Trace.WriteLine(s.nachname);
            }
        }

        


        private void txtName_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != System.Windows.Input.Key.Enter) return;
            e.Handled = true;
            btnAdd_Click(this, new RoutedEventArgs());
        }


        private void appendData(dynamic list)
        {
            List<Person> items = new List<Person>();
            if (list != null)
            {
                foreach (Person people in list)
                {
                    var date = new DateTime();
                    Button btnRemove = new Button();
                    Person p = new Person() { vorname = people.vorname, nachname = people.nachname, geburtstag = people.geburtstag, alter = people.alter, id = people.id };
                    items.Add(p);
                    try
                    {
                        string dateInput = people.geburtstag;
                        date = DateTime.Parse(dateInput);
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine(ex.Message);
                    }
                }
            }
            lvUsers.ItemsSource = items;
        }
        private void btnApi_Click(object sender, RoutedEventArgs e)
        {
            load();
        }

        private async void btnRemove_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                HttpClient httpClient = new HttpClient();

                Person s = (Person)lvUsers.SelectedItems[0];
                string id = s.id;
                string url = "http://localhost:3001/remove/" + id;
                HttpResponseMessage response = await httpClient.DeleteAsync(url);
                httpClient.Dispose();
                load();
            } catch(Exception ex)
            {
                MessageBox.Show("Bitte wählen Sie ein Element aus, das sie löschen wollen!");
            }

        }

        private void TextBox_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != System.Windows.Input.Key.Enter) return;
            e.Handled = true;
            Trace.WriteLine(txtVorname.Text);
            //btnApi_Click(this, new RoutedEventArgs());
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void lvUsers_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var item = (sender as ListView).SelectedItem;
            if (item == null)
            {
                btnRemove.IsEnabled = false;
            }
        }

        private void btnFind_Click(object sender, RoutedEventArgs e)
        {
            string url = "http://localhost:3001/find";
            bool isFirst = true;
            if (!string.IsNullOrEmpty(searchVorname.Text))
            {
                if (isFirst)
                {
                    url += "?vorname=" + searchVorname.Text;
                    isFirst = false;
                }
                else url += "&vorname=" + searchVorname.Text;
            }
            if (!string.IsNullOrEmpty(searchNachname.Text))
            {
                if (isFirst)
                {
                    url += "?nachname=" + searchNachname.Text;
                    isFirst = false;
                }
                else url += "&nachname=" + searchNachname.Text;
            }
            if (!string.IsNullOrEmpty(searchTag.Text))
            {
                if (isFirst)
                {
                    url += "?tag=" + searchTag.Text;
                    isFirst = false;
                }
                else url += "&tag=" + searchTag.Text;
            }
            if (!string.IsNullOrEmpty(searchMonat.Text))
            {
                if (isFirst)
                {
                    url += "?monat=" + searchMonat.Text;
                    isFirst = false;
                }
                else url += "&monat=" + searchMonat.Text;
            }
            if (!string.IsNullOrEmpty(searchJahr.Text))
            {
                if (isFirst)
                {
                    url += "?jahr=" + searchJahr.Text;
                    isFirst = false;
                }
                else url += "&jahr=" + searchJahr.Text;
            }
            if (!string.IsNullOrEmpty(searchAlter.Text))
            {
                if (isFirst)
                {
                    url += "?alter=" + searchAlter.Text;
                    isFirst = false;
                }
                else url += "&alter=" + searchAlter.Text;
            }

            Trace.WriteLine(url);
            HttpClient client = new HttpClient();
            string data = client.GetStringAsync(url).Result;
            var list = JsonSerializer.Deserialize<List<Person>>(data);
            appendData(list);
        }

        private void txtVorname_TextChanged(object sender, KeyEventArgs e)
        {

        }


        private void txtNachname_KeyUp(object sender, TextChangedEventArgs e)
        {

        }

        private void txtTag_KeyUp(object sender, TextChangedEventArgs e)
        {

        }

        private void txtMonat_KeyUp(object sender, TextChangedEventArgs e)
        {

        }

        private void txtJahr_KeyUp(object sender, TextChangedEventArgs e)
        {

        }

        private void txtVorname_TextChanged(object sender, TextChangedEventArgs e)
        {

        }
    }
}
