using System.Threading.Tasks;

namespace LostAnimalsAPI.Helpers.Base
{
    public interface IFileHelper
    {

        Task<byte[]> LoadFileAsync(long id, ObjectType type, bool fullSize = true);

        void SaveFile(long id, byte[] source, ObjectType type);

        public enum ObjectType
        {
            User, Post, Shelter
        }
    }
}
