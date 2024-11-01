using MongoDB.Driver;
using Raven.Message.Domain.Entities;
using Raven.Message.Infrastructure;

namespace Raven.Message.Application.Handlers;

public class ChatHandler(MongoContext context)
{
    private readonly IMongoCollection<Chat> _chats = context.GetCollection<Chat>("Chats");

    public async Task<string> CreateSimpleChatAsync(string userOne, string userTwo)
    {
        var newChat = new Chat
        {
            Participants = [userOne, userTwo],
        };
        await _chats.InsertOneAsync(newChat);
        
        return newChat.ChatId;
    }
    
    public async Task AddUserAsync(string userId, string chatId)
    {
        var chat = await _chats.Find(c => c.ChatId == chatId).FirstOrDefaultAsync();

        if (chat != null)
        {
            if (!chat.Participants.Contains(userId))
            {
                chat.Participants.Add(userId);
                await _chats.ReplaceOneAsync(c => c.ChatId == chatId, chat);
            }
        }
        else
        {
            var newChat = new Chat
            {
                Participants = [userId]
            };
            await _chats.InsertOneAsync(newChat);
        }
    }
    
    public async Task<Chat> GetChatDetailsAsync(string chatId)
    {
        var chat = await _chats.Find(c => c.ChatId == chatId).FirstOrDefaultAsync();
        return chat;
    }

}