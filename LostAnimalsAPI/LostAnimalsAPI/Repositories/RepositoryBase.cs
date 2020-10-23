using LostAnimalsAPI.Database;
using LostAnimalsAPI.Models.Base;
using LostAnimalsAPI.Repositories.Interfaces;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Repositories
{
    public class RepositoryBase<T> : IRepositoryBase<T> where T : class, IModelBase
    {
        private AnimalsDbContext _dbContext;

        public RepositoryBase(AnimalsDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async Task<IEnumerable<T>> Get()
        {
            return _dbContext.Set<T>();
        }
    }
}
