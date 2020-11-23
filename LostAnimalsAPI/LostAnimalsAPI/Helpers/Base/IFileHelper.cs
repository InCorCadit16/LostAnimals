﻿using System.Threading.Tasks;

namespace LostAnimalsAPI.Helpers.Base
{
    public interface IFileHelper
    {

        Task<byte[]> LoadFileAsync(long id, bool forPost = true, bool fullSize = true);
    }
}