# 🚀 MyClassRoom API - Free Deployment Guide

## Option 1: Railway.app (Recommended - Easiest!)

### Prerequisites
- GitHub account
- Railway account (free): https://railway.app

### Steps:

1. **Push your code to GitHub** (if not already)
   ```bash
   git init
   git add .
   git commit -m "Prepare for deployment"
   git remote add origin YOUR_GITHUB_REPO_URL
   git push -u origin main
   ```

2. **Deploy to Railway**
   - Go to https://railway.app
   - Click "Start a New Project"
   - Select "Deploy from GitHub repo"
   - Choose your MyClassRoom repository
   - Railway will auto-detect the project and start building

3. **Add PostgreSQL Database**
   - In your Railway project dashboard
   - Click "New" → "Database" → "PostgreSQL"
   - Railway automatically creates `DATABASE_URL` environment variable

4. **Set Environment Variables**
   - Go to your service → "Variables" tab
   - Railway auto-sets `DATABASE_URL` from PostgreSQL
   - Add these additional variables:
     - `PORT` = 8080 (optional, Railway auto-assigns)
     - `JWT_SECRET` = your-secret-key
     - `JWT_DOMAIN` = your-domain
     - `JWT_AUDIENCE` = jwt-audience
     - `JWT_REALM` = ktor sample app

5. **Deploy!**
   - Railway auto-deploys on every git push
   - Get your public URL from the deployment dashboard

### Cost: **FREE**
- 500 hours/month (always-on for ~20 days)
- 1GB RAM, 1 vCPU
- PostgreSQL database included

---

## Option 2: Render.com

### Steps:

1. **Push to GitHub** (same as above)

2. **Create Web Service on Render**
   - Go to https://render.com
   - Click "New" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     - **Build Command**: `./gradlew installDist`
     - **Start Command**: `./build/install/MyClassRoom/bin/MyClassRoom`

3. **Add PostgreSQL Database**
   - Click "New" → "PostgreSQL"
   - Copy the "Internal Database URL"

4. **Set Environment Variables**
   - In Web Service settings → "Environment"
   - Add:
     - `DATABASE_URL` = (from PostgreSQL internal URL)
     - `DB_USER` = (from database credentials)
     - `DB_PASSWORD` = (from database credentials)
     - `PORT` = 10000
     - `JWT_SECRET` = your-secret-key

5. **Deploy!**

### Cost: **FREE**
- Free tier available
- Auto-sleeps after 15 mins of inactivity
- PostgreSQL free tier: 90 days

---

## Option 3: Fly.io

### Steps:

1. **Install Fly CLI**
   ```bash
   # Windows (PowerShell)
   iwr https://fly.io/install.ps1 -useb | iex
   ```

2. **Login and Initialize**
   ```bash
   fly auth login
   fly launch
   ```

3. **Create PostgreSQL**
   ```bash
   fly postgres create
   fly postgres attach YOUR_POSTGRES_APP_NAME
   ```

4. **Deploy**
   ```bash
   fly deploy
   ```

### Cost: **FREE**
- 3 shared-cpu VMs
- 256MB RAM per VM
- 3GB PostgreSQL storage

---

## Testing Your Deployed API

Once deployed, test your endpoints:

```bash
# Replace YOUR_DEPLOYED_URL with your actual URL

# Health check
curl https://YOUR_DEPLOYED_URL/

# Register user
curl -X POST https://YOUR_DEPLOYED_URL/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"password123","role":"STUDENT"}'

# Login
curl -X POST https://YOUR_DEPLOYED_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

---

## 📝 Notes

- **Railway.app** is the easiest - one-click deploy, auto-detects everything
- **Render.com** is good but has cold starts (sleeps when inactive)
- **Fly.io** requires CLI but gives more control

## 🎯 Recommendation

For the **easiest deployment**, use **Railway.app**:
1. Push to GitHub
2. Connect Railway to your repo
3. Add PostgreSQL
4. Done! ✅

Your API will be live at: `https://your-app-name.railway.app`
