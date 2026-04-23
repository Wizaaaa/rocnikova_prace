import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'
import { JWT } from 'https://esm.sh/google-auth-library@9'

Deno.serve(async (req) => {
  // 1. Load JSON from Secrets
  const serviceAccount = JSON.parse(Deno.env.get('FIREBASE_SERVICE_ACCOUNT')!)

  // 2. Google OAuth2
  const jwt = new JWT({
    email: serviceAccount.client_email,
    key: serviceAccount.private_key,
    scopes: ['https://www.googleapis.com/auth/cloud-platform'],
  })
  const tokens = await jwt.authorize()

  // 3. Initialize Supabase to get tokens
  const supabase = createClient(
    Deno.env.get('SUPABASE_URL')!,
    Deno.env.get('SUPABASE_SERVICE_ROLE_KEY')!
  )

  const { data: users } = await supabase
    .from('profiles')
    .select('fcm_token, name')
    .eq('notifications_enabled', true)
    .not('fcm_token', 'is', null)

  // 4. Send notification
  const projectId = serviceAccount.project_id
  const url = `https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`

  const requests = users?.map(async (user) => {
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${tokens.access_token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        message: {
          token: user.fcm_token,
          notification: {
            title: `Ahoj ${user.name || 'studente'}! 📚`,
            body: 'Dnes jsi ještě neprocvičoval. Dej si aspoň jeden rychlý test!',
          }
        }
      })
    })
    const resData = await res.json()
    console.log(`Výsledek pro ${user.name}:`, resData)
    return res
  })

  await Promise.all(requests || [])

  return new Response("Notifikace odeslány", { status: 200 })
})