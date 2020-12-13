using LostAnimalsAPI.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Database.Configurations
{
     public class ShelterConfig : IEntityTypeConfiguration<Shelter>
     {
          public void Configure(EntityTypeBuilder<Shelter> builder)
          {
               builder.HasOne(c => c.Location).WithOne(l => l.Shelter).HasForeignKey<Location>(l => l.ShelterId);
          }
     }
}
