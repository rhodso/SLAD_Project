#Imports
import discord
import asyncio
from discord.ext.commands import Bot
from discord.ext import commands
import platform
import datetime
import random

#Set prefix character
prefix = "!"
total = 0

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
    log('Initialising...')
    log('Logged in as '+client.user.name+' (ID:'+client.user.id+') \n Connected to '+str(len(client.servers))+' servers and '+str(len(set(client.get_all_members())))+' users')
    log('')
    log('Current Discord.py Version: {} \n Current Python Version: {}'.format(discord.__version__, platform.python_version()))
    log('Ready!')
    return await client.change_presence(game=discord.Game(name='')) 

#Commands
@client.event
async def on_message(message):
    if(message.content[:1] == prefix):

        #Help command
        if(message.content == (prefix + "help")):
            log('Running help command...')
            await client.send_message(client.get_channel(message.channel.id),'The prefix character is currently ' + prefix + 
            ', \nCommand list: '+
            '\n  ping - pings the bot '+
            '\n  flipacoin - flips a coin'+
            '\n  whatisthemeaningoflife - tells you the true meaning of life'+
            '\n  pickrandom - picks a random choice between a list of choices'+
            '\n    usage: pickrandom|ChoiceA|ChoiceB|...|ChoiceN'+
            '\n  help - shows this message'
            )

        #Ping command
        if(message.content == (prefix + "ping")):
            log('Running ping command...')
            await client.send_message(client.get_channel(message.channel.id), 'Pong!')

        #What is the meaning of life command
        if(message.content == (prefix + "whatisthemeaningoflife")):
            log('Running meaningofLife command....')
            await client.send_message(client.get_channel(message.channel.id), 'The meaning of life is... ')
            await client.send_message(client.get_channel(message.channel.id), '42')

        #Flip a coin command
        if(message.content == (prefix + "flipacoin")):
            log('Running flipacoin command...')
            if(random.randint(0,1) == 0):
                await client.send_message(client.get_channel(message.channel.id), 'It\'s Heads!')
            else:
                await client.send_message(client.get_channel(message.channel.id), 'It\'s Tails!')

        #PickRandom command
        if(message.content[:11] == (prefix + "pickrandom")):
            log('Running pickrandom command....')
            choices = message.content.split('|')
            choice = random.randint(1,len(choices)-1)
            await client.send_message(client.get_channel(message.channel.id), 'Hmm, I choose ' + choices[choice])

    else:
        #Message is not a command, ignore
        pass

#Run bot
client.run(str(getToken()))