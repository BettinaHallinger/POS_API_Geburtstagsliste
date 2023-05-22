using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WpfApp1
{
    public class Person
    {
        public string vorname { get; set; }
        public string nachname { get; set; }
        public string geburtstag { get; set; }
        public int tag { get; set; }
        public int monat { get; set; }
        public int jahr { get; set; }

        public int alter { get; set; }

        public string id { get; set; }

        public Person()
        {
            
        }
    }
}

