using LostAnimalsAPI.Models;
using Microsoft.EntityFrameworkCore;
using System.Reflection;

namespace LostAnimalsAPI.Database
{
    public class AnimalsDbContext: DbContext
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
    }
}
