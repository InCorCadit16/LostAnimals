using LostAnimalsAPI.Models.Base;
using LostAnimalsAPI.Services;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Repositories.Interfaces
{
    public interface IRepositoryBase<T> where T: IModelBase
    {

        Task<IEnumerable<T>> Get();
    }
}
