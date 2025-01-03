using MongoDB.Driver;
using Raven.Message.Domain.Entities;
using Raven.Message.Infrastructure;

namespace Raven.Message.Application.Handlers;

public class UserChatHandler(MongoContext context)
{
    private readonly IMongoCollection<UserChat> _userChats = context.GetCollection<UserChat>("UserChats");

    public async Task BindUserToChatAsync(string userId, string chatId)
    {
        var userChat = await _userChats.Find(uc => uc.UserId == userId).FirstOrDefaultAsync();

        if (userChat != null)
        {
            if (!userChat.ChatIds.Contains(chatId))
            {
                userChat.ChatIds.Add(chatId);
                await _userChats.ReplaceOneAsync(uc => uc.UserId == userId, userChat);
            }
        }
        else
        {
            var newUserChat = new UserChat
            {
                UserId = userId,
                ChatIds = [chatId]
            };
            await _userChats.InsertOneAsync(newUserChat);
        }
    }
    
    public async Task<List<string>> GetChatsByUserId(string userId)
    {
        var userChat = await _userChats.Find(uc => uc.UserId == userId).FirstOrDefaultAsync();
        
        return userChat?.ChatIds ?? [];
    }
}
