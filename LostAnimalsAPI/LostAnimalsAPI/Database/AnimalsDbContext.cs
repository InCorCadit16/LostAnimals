using LostAnimalsAPI.Models;
using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using System.Reflection;

namespace LostAnimalsAPI.Database
{
    public class AnimalsDbContext: IdentityDbContext<ApplicationUser, Role, long>
    {

        public AnimalsDbContext(DbContextOptions<AnimalsDbContext> options) : base(options)
        {

        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {

        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());
        }

        public DbSet<SpeciesLookup> SpeciesLookup { get; set; }
    }
}
