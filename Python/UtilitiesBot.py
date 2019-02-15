import discord
import asyncio
from discord.ext.commands import Bot
from discord.ext import commands
import platform
import datetime

prefix = '!'

#Def log method
def log(Message):
    print(str(datetime.datetime.now())+ '   ' + str(Message))

#Def Method to read token from the file
def getToken():
    tokenFile = open('token.txt','r')
    token = tokenFile.read()
    return token

#Define client
client = Bot(description='', command_prefix=prefix, pm_help = False)

#Startup sequence
@client.event
async def on_ready():
    print("")
    log('Initialising...')
    log('Logged in as '+client.user.name+' (ID:'+client.user.id+') \n Connected to '+str(len(client.servers))+' servers and '+str(len(set(client.get_all_members())))+' users')
    log('')
    log('Current Discord.py Version: {} \n Current Python Version: {}'.format(discord.__version__, platform.python_version()))
    log('Ready!\n\n')
    return await client.change_presence(game=discord.Game(name='')) 

#Commands
@client.event
async def on_message(message):
    if(message == prefix + "help"):
        log('Running help command...')
        await client.send_message(client.get_channel(message.channel.id), 'Here\'s some text')

    elif(message == prefix + "ping"):
        log('Running ping command...')
        await client.send_message(client.get_channel(message.channel.id), 'Pong!')

#Run bot
client.run(str(getToken()))