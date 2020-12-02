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

        public DbSet<Post> Posts { get; set; }

        public DbSet<Location> Locations { get; set; }

        public DbSet<SpeciesLookup> Species { get; set; }

        public DbSet<ColorLookup> Colors { get; set; }

        public DbSet<BreedLookup> Breeds { get; set; }

        public DbSet<Comment> Comments { get; set; }
    }
}
