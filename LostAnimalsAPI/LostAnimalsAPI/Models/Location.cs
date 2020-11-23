﻿
namespace LostAnimalsAPI.Models
{
    public class Location:ModelBase
    {
        public string Address { set; get; }
        public double Latitude { set; get; }
        public double Longitude {set; get;}

        public long PostId { get; set; }
        public Post Post { get; set; }
    }
}
